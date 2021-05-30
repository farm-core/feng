package com.yun.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yun.bean.admin.YunUser;
import com.yun.bean.base.Result;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author wu_xufeng
 * @since 2020-11-06
 */
public interface IYunUserService extends IService<YunUser> {

    Result addRegister(YunUser yunUser);

    Result getVerifyCode();

    Result login(String username, String password, String verifyCode, String loginType);

    Result logout(String username);

    Result update(String username, String password, String newpassword);

    Result getMailCode(String email, String userName, HttpServletRequest request);

    Result<YunUser> getByUsername(String username);
}
