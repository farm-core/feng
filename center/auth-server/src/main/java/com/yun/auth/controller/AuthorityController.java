package com.yun.auth.controller;

import com.yun.bean.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @program: datacenter
 * @description:
 * @author: wxf
 * @create: 2019-05-20 09:40
 **/

@RestController
@Slf4j
public class AuthorityController {

    /*private AuthenticationService authenticationService;

    public AuthorityController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }*/

    @RequestMapping("/auth/permission")
    public Result decide(@RequestParam String url,
                         @RequestParam String method,
                         HttpServletRequest request) {
        log.debug("url:{},method:{}", url, method);
//        HttpServletRequestAuthWrapper wrapper = new HttpServletRequestAuthWrapper(request, url, method);
//        return authenticationService.decide(wrapper);
        return null;
    }

    @RequestMapping("/security/permission")
    public Result permission(@RequestParam String url,
                             @RequestParam String method,
                             HttpServletRequest request) {
        log.debug("url:{},method:{}", url, method);
//        HttpServletRequestAuthWrapper wrapper = new HttpServletRequestAuthWrapper(request, url, method);
//        return authenticationService.decide(wrapper);
        return null;
    }
}
