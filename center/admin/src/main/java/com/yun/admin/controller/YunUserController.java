package com.yun.admin.controller;


import com.yun.admin.context.UserContext;
import com.yun.admin.service.IYunUserService;
import com.yun.bean.admin.YunUser;
import com.yun.bean.base.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Pattern;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wu_xufeng
 * @since 2020-11-06
 */
@Slf4j
@RefreshScope
@RestController
@Api("用户信息接口API")
@RequestMapping("/admin/yun-user")
public class YunUserController {

    @Autowired
    private IYunUserService iYunUserService;

    @PostMapping("/addRegister")
    @ApiOperation("用户注册")
    public Result addRegister(@RequestBody @ApiParam("用户实体封装") YunUser yunUser) {
        return iYunUserService.addRegister(yunUser);
    }

    @PostMapping("/logins")
    @ApiOperation("用户登陆")
    public Result login(@RequestParam("username") @ApiParam(value = "登陆用户名", example = "zhangsan") String username,
                        @RequestParam("password") @ApiParam(value = "登陆用户密码", example = "zhang_san_123")
                        @Pattern(regexp = "^[a-zA-Z]\\w{5,17}$",
                                message = "以字母开头，长度在6~18之间，只能包含字符、数字和下划线") String password,
                        @RequestParam("verifyCode") @ApiParam(value = "登陆验证码") String verifyCode,
                        @RequestParam("loginType") @ApiParam(value = "登陆方式", example = "1-账号密码/2-手机号/3-邮箱") String loginType) {
        return iYunUserService.login(username, password, verifyCode, loginType);
    }

    @PostMapping("/update")
    @ApiOperation("修改密码")
    public Result update(@RequestParam("username") @ApiParam(value = "登陆用户名", example = "zhangsan") String username,
                         @RequestParam("password") @ApiParam(value = "原登陆用户密码", example = "zhang_san_123")
                         @Pattern(regexp = "^[a-zA-Z]\\w{5,17}$",
                                 message = "以字母开头，长度在6~18之间，只能包含字符、数字和下划线") String password,
                         @RequestParam("newpassword") @ApiParam(value = "新登陆用户密码", example = "zhang_san_123")
                         @Pattern(regexp = "^[a-zA-Z]\\w{5,17}$",
                                 message = "以字母开头，长度在6~18之间，只能包含字符、数字和下划线") String newpassword) {
        return iYunUserService.update(username, password, newpassword);
    }

    @ApiOperation("退出登陆")
    @PostMapping("/logout")
    public Result logout() {
        String username = UserContext.getInstance().getUserName();
        return iYunUserService.logout(username);
    }

    @GetMapping("/getVerifyCode")
    @ApiOperation("获取图片验证码")
    public Result getVerifyCode() {
        return iYunUserService.getVerifyCode();
    }

    @PostMapping("/getMailCode")
    @ApiOperation("获取邮箱验证码")
    public Result getMailCode(@RequestParam("email") String email,
                              @RequestParam("userName") String userName,
                              HttpServletRequest request) {
        return iYunUserService.getMailCode(email, userName, request);
    }

    @GetMapping("/getByUsername")
    @ApiOperation("根据用户名获取用户信息")
    public Result<YunUser> getByUsername(@RequestParam("username") @ApiParam(value = "登陆用户名", example = "zhangsan") String username) {
        return iYunUserService.getByUsername(username);
    }
}
