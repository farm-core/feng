package com.yun.admin.interceptor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yun.admin.context.UserContext;
import com.yun.admin.exception.OperationException;
import com.yun.bean.admin.YunUser;
import com.yun.bean.enums.OperationError;
import com.yun.idb.mapper.YunUserMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * @ClassName UserInterceptor
 * @Description 用户拦截器
 * @Auther wu_xufeng
 * @Date 2020/11/9
 * @Version 1.0
 */
public class UserInterceptor implements HandlerInterceptor {

    @Autowired
    private YunUserMapper yunUserMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String loginName = request.getHeader("userName");
        if (StringUtils.isNotBlank(loginName)) {
            QueryWrapper<YunUser> wrapper = new QueryWrapper<>();
            wrapper.eq("user_name", loginName);
            YunUser yunUser = yunUserMapper.selectOne(wrapper);
            if (Objects.isNull(yunUser)) {
                throw new OperationException(OperationError.USERNAEM_ERROR);
            }
            UserContext.getInstance().set(yunUser);
        } else {
            YunUser yunUser = new YunUser();
            yunUser.setUserName("测试默认用户");
            yunUser.setGroupId(1);
            yunUser.setUserName("test");
            yunUser.setId(1);
            UserContext.getInstance().set(yunUser);
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContext.getInstance().remove();
    }
}
