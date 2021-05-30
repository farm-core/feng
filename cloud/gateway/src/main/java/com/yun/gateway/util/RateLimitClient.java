package com.yun.gateway.util;

import com.yun.gateway.barrels.RateLimitVo;
import com.yun.gateway.config.RedisInitConfig;
import com.yun.gateway.enmu.RateLimitMethod;
import com.yun.gateway.enmu.RateLimitResult;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;

/**
 * @author: wxf
 * 操作令牌桶的实现类
 **/
@Slf4j
@Service
public class RateLimitClient {

    /**
     * 日志信息
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisInitConfig.class);

    /**
     * 令牌桶服务名前缀
     */
    private static final String RATE_LIMIT_PREFIX = "rateLimter:";

    /**
     * 操作redis的工具类
     */
    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 执行lua脚本的bean，在Redis初始化时注入
     */
    @Resource
    @Qualifier("rateLimitLua")
    RedisScript<Long> rateLimitScript;


    /**
     * @return user.enums.RateLimitResult
     * @description 初始化令牌桶
     * @Param [key 初始化令牌桶服务的名称, rateLimitInfo 令牌桶vo]
     * @author wxf
     */
    public RateLimitResult init(String key, RateLimitVo rateLimitInfo) {
        return exec(key, RateLimitMethod.init,
                rateLimitInfo.getInitialPermits(),
                rateLimitInfo.getMaxPermits(),
                rateLimitInfo.getOneSecondNum(),
                key);
    }


    /**
     * @return user.enums.RateLimitResult
     * @description 修改令牌桶的配置信息
     * @Param [key  需要修改令牌桶配置信息服务的名称, rateLimitInfo 令牌桶vo]
     * @author wxf
     */
    public RateLimitResult modify(String key, RateLimitVo rateLimitInfo) {
        return exec(key, RateLimitMethod.modify, key,
                rateLimitInfo.getMaxPermits(),
                rateLimitInfo.getOneSecondNum());
    }


    /**
     * @return user.enums.RateLimitResult
     * @description 删除令牌桶的配置信息
     * @Param [key 需要删除服务的名称]
     * @author wxf
     */
    public RateLimitResult delete(String key) {
        return exec(key, RateLimitMethod.delete);
    }


    /**
     * @return user.enums.RateLimitResult
     * @description 每次请求获得的令牌，默认1
     * @Param [key 需要获得令牌的服务名称]
     * @author wxf
     */
    public RateLimitResult acquire(String key) {
        return acquire(key, 1);
    }

    public RateLimitResult acquire(String key, Integer permits) {
        return exec(key, RateLimitMethod.acquire, permits);
    }

    /**
     * 执行redis的具体方法，lua脚本执行redis修改值操作
     *
     * @param key
     * @param method
     * @param params
     * @return
     */
    private RateLimitResult exec(String key, RateLimitMethod method, Object... params) {
        try {
            Long timestamp = getRedisTimestamp();
            String[] allParams = new String[params.length + 2];
            allParams[0] = method.name();
            allParams[1] = timestamp.toString();
            for (int index = 0; index < params.length; index++) {
                allParams[2 + index] = params[index].toString();
            }
            Long result = redisTemplate.execute(rateLimitScript,
                    Collections.singletonList(getKey(key)),
                    allParams);
            return RateLimitResult.getResult(result);
        } catch (Exception e) {
            log.error("执行脚本失败, key:{}, method:{}",
                    key, method.name(), e);
            return RateLimitResult.ERROR;
        }
    }


    /**
     * @return java.lang.Long
     * @description 获取令牌桶的计时时间
     * @Param []
     * @author wxf
     */
    private Long getRedisTimestamp() {
        Long currMillSecond = redisTemplate.execute(
                (RedisCallback<Long>) redisConnection -> redisConnection.time()
        );
        return currMillSecond;
    }


    /**
     * @return java.lang.String 完整的令牌桶服务名
     * @description 获取令牌桶的服务名
     * @Param [key 令牌桶的服务名称]
     * @author wxf
     */
    private String getKey(String key) {
        return RATE_LIMIT_PREFIX + key;
    }


    /**
     * @return boolean 获取成功返回true，失败返回false
     * @description 执行获取令牌操作
     * @Param [clientName 服务名称]
     * @author wxf
     */
    public boolean execute(String clientName) {
        //执行获取令牌
        RateLimitResult result = acquire(clientName);

        //定义处理日志
        String msg = "服务：" + clientName + "，";

        //返回类型 默认false
        boolean isGet = false;

        //获取结果失败打印失败信息并反回false
        if (result == RateLimitResult.ACQUIRE_FAIL) {
            msg += "获取令牌失败";
        } else if (result == RateLimitResult.NO_LIMIT) {
            msg += "未配置令牌桶信息";
        } else if (result == RateLimitResult.MODIFY_ERROR) {
            msg += "修改令牌桶配置错误";
        } else if (result == RateLimitResult.UNSUPPORT_METHOD) {
            msg += "不支持的操作";
        } else if (result == RateLimitResult.ERROR) {
            msg += "获取令牌意外出错";
        } else if (result == RateLimitResult.SUCCESS) {
            msg += "获取令牌成功";
            isGet = true;
        }

        //打印日志
        LOGGER.debug(msg);
        return isGet;
    }
}