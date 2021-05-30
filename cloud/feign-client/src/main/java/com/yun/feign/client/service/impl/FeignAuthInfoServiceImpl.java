package com.yun.feign.client.service.impl;

import com.yun.bean.result.Result;
import com.yun.feign.client.service.FeignAuthInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @ClassName FeignAuthInfoServiceImpl
 * @Description TODO
 * @Auther wu_xufeng
 * @Date 2020/12/27
 * @Version 1.0
 */
@Slf4j
@Component
public class FeignAuthInfoServiceImpl implements FeignAuthInfoService {
    @Override
    public Result authClient(String authentication, String url, String method) {
        return Result.fail();
    }

    @Override
    public Result authUser(String authentication, String url, String method) {
        return Result.fail();
    }
}
