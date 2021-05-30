package com.yun.admin.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yun.admin.exception.OperationException;
import com.yun.admin.service.IYunUserService;
import com.yun.admin.utils.ProjectUtil;
import com.yun.bean.admin.YunUser;
import com.yun.bean.base.Result;
import com.yun.bean.base.context.AccessToken;
import com.yun.bean.base.context.HttpServletContext;
import com.yun.bean.enums.OperationError;
import com.yun.common.VerifyCode;
import com.yun.common.encrypt.RSAUtil;
import com.yun.common.utils.HardwareInfo;
import com.yun.common.utils.MailUtil;
import com.yun.idb.mapper.YunUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.MacSigner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wu_xufeng
 * @since 2020-11-06
 */
@Slf4j
@Service
public class IYunUserServiceImpl extends ServiceImpl<YunUserMapper, YunUser> implements IYunUserService {

    @Autowired
    private YunUserMapper yunUserMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private HttpServletContext httpServletContext;

    @Value("${spring.security.oauth2.jwt.signingKey}")
    private String key;

    /**
     * 注册
     *
     * @param yunUser
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result addRegister(YunUser yunUser) {
        HttpSession session = httpServletContext.getSession();
        String code = session.getAttribute("yzm").toString();

        if (Objects.nonNull(yunUser)) {
            if (Objects.nonNull(session.getAttribute("userName"))) {
                if (StringUtils.isBlank(yunUser.getEmailNum())) {
                    throw new OperationException(OperationError.MAIL_CODE_NULL);
                } else {
                    if (!yunUser.getEmailNum().equals(session.getAttribute("userName").toString())) {
                        throw new OperationException(OperationError.MAIL_CODE_ERROE);
                    }
                }
            } else {
                throw new OperationException(OperationError.MAIL_CODE_TIME_OUT);
            }

            // 密码格式
            String password = yunUser.getPassword();
            if (StringUtils.isNotBlank(password)) {
                String regexp = "^[a-zA-Z]\\w{5,17}$";
                Matcher matcher = Pattern.compile(regexp).matcher(password);
                if (!matcher.find()) {
                    return Result.fail(OperationError.PASSWORD_FORMAT_ERROR);
                }
            }

            // 验证码
            if (StringUtils.isBlank(yunUser.getVerifyCode()) || (StringUtils.isNotBlank(code) && !code.equals(yunUser.getVerifyCode()))) {
                return Result.fail(OperationError.VERIFICATION_CODE);
            }
        }
        // 判断注册用户是否存在
        QueryWrapper<YunUser> wrapper = new QueryWrapper<>();
        wrapper.eq("user_name", yunUser.getUserName());
        List<YunUser> yunUsers = yunUserMapper.selectList(wrapper);
        if (!CollectionUtils.isEmpty(yunUsers)) {
            throw new OperationException(OperationError.USERNAME_EXIST);
        }

        // 用户密码加密  ----  公钥加密/私钥解密
        try {
            String password = RSAUtil.encrypt(yunUser.getPassword(), RSAUtil.publicKey);
            yunUser.setPassword(password);
        } catch (Exception e) {
            log.error("======密码加密失败======", e);
            return Result.fail();
        }

        ProjectUtil.setOperator(yunUser, ProjectUtil.SET_TYPE);
        int insert = yunUserMapper.insert(yunUser);
        if (insert > 0) {
            return Result.success();
        } else {
            return Result.fail();
        }
    }

    /**
     * 获取验证码
     *
     * @return
     */
    @Override
    public Result getVerifyCode() {
        try {
            int width = 200;
            int height = 69;
            //生成对应宽高的初始图片
            BufferedImage verifyImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            //功能是生成验证码字符并加上噪点，干扰线，返回值为验证码字符
            String randomText = VerifyCode.drawRandomText(width, height, verifyImg);
            // 往session中放入yzm的信息
            HttpServletContext httpServletContext = new HttpServletContext();
            httpServletContext.getSession().setAttribute("yzm", randomText);
            // 把图片转为base64封装到json中
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ImageIO.write(verifyImg, "png", stream);
            String base64 = Base64.getEncoder().encodeToString(stream.toByteArray());
            JSONObject obj = new JSONObject();
            obj.put("img", base64);
            stream.close();
            return Result.success(obj);
        } catch (Exception e) {
            log.error("验证码获取异常：", e);
        }
        return Result.fail();
    }

    /**
     * 用户登陆
     *
     * @param username   用户名
     * @param password   用户密码
     * @param verifyCode 验证码
     * @param loginType  登陆方式
     * @return
     */
    @Override
    public Result login(String username, String password, String verifyCode, String loginType) {
        QueryWrapper<YunUser> wrapper = new QueryWrapper<>();
        wrapper.eq("user_name", username);
        YunUser yunUser = yunUserMapper.selectOne(wrapper);

        if (Objects.isNull(yunUser)) {
            throw new OperationException(OperationError.USERNAEM_ERROR);
        }
        String pw = "";
        try {
            pw = RSAUtil.decrypt(yunUser.getPassword(), RSAUtil.privateKey);
        } catch (Exception e) {
            log.error("======密码解密失败======", e);
        }
        if (StringUtils.isNotBlank(pw) && !pw.equals(password)) {
            throw new OperationException(OperationError.PASSWD_ERROR);
        }
        if ("0".equals(yunUser.getEnabled())) {
            throw new OperationException(OperationError.USER_NON_ENABLED);
        }
        if ("0".equals(yunUser.getAccountNonLocked())) {
            throw new OperationException(OperationError.USER_LOCKED);
        }
        if ("0".equals(yunUser.getAccountNonExpired())) {
            throw new OperationException(OperationError.USER_EXPIRED);
        }
        if ("0".equals(yunUser.getCredentialsNonExpired())) {
            throw new OperationException(OperationError.USER_CRE_EXPRIRED);
        }

        // 利用jwt生成token并存储到redis中
        MacSigner macSigner = new MacSigner(key);
        Jwt encode = JwtHelper.encode(username + ":" + password, macSigner);
        String encoded = encode.getEncoded();
        Calendar instance = Calendar.getInstance();
        Date date = instance.getTime();
        long time = date.getTime();
        time = time + 1000 * 60 * 30;
        date = new Date(time);
        redisTemplate.opsForSet().add(username, encoded);
        redisTemplate.expire(username, 30, TimeUnit.MINUTES);

        AccessToken accessToken = new AccessToken();
        accessToken.setToken(encoded);
        accessToken.setExpiration(date);

        //TODO 后续需要加上部门，角色，菜单等等信息

        return Result.success(accessToken);
    }

    /**
     * 退出登陆
     *
     * @param username
     */
    @Override
    public Result logout(String username) {
        Long remove = redisTemplate.opsForSet().remove(username);
        if (remove > 0) {
            return Result.success();
        } else {
            return Result.fail();
        }
    }

    /**
     * 修改用户密码
     *
     * @param username
     * @param password
     * @param newpassword
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result update(String username, String password, String newpassword) {
        QueryWrapper<YunUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name", username);
        YunUser yunUser = yunUserMapper.selectOne(queryWrapper);

        UpdateWrapper<YunUser> wrapper = new UpdateWrapper<>();
        if (StringUtils.isNotBlank(password) && StringUtils.isNotBlank(newpassword)) {
            // 校验密码是否正确
            String pw = "";
            try {
                pw = RSAUtil.decrypt(yunUser.getPassword(), RSAUtil.privateKey);
            } catch (Exception e) {
                log.error("======密码解密失败======", e);
            }
            if (StringUtils.isNotBlank(pw) && !pw.equals(password)) {
                throw new OperationException(OperationError.PASSWD_ERROR);
            }

            // 校验新密码格式
            if (StringUtils.isNotBlank(newpassword)) {
                String regexp = "^[a-zA-Z]\\w{5,17}$";
                Matcher matcher = Pattern.compile(regexp).matcher(newpassword);
                if (!matcher.find()) {
                    return Result.fail(OperationError.PASSWORD_FORMAT_ERROR);
                }
            }

            try {
                newpassword = RSAUtil.encrypt(newpassword, RSAUtil.publicKey);
            } catch (Exception e) {
                log.error("======密码加密失败======", e);
                throw new OperationException(OperationError.PASSWD_CIPHERTEXT_ERROR);
            }
            wrapper.set("password", newpassword);
        }
        wrapper.eq("user_name", username);
        int byId = yunUserMapper.update(yunUser, wrapper);
        if (byId > 0) {
            return Result.success();
        } else {
            return Result.fail();
        }
    }

    /**
     * 获取邮箱验证码
     *
     * @param email
     * @return
     */
    @Override
    public Result getMailCode(String email, String userName, HttpServletRequest request) {
        if (StringUtils.isNotBlank(email)) {
            if (!MailUtil.isEmail(email)) {
                throw new OperationException(OperationError.MAIL_FORMAT_ERROR);
            }

            int emailCode = (int) ((Math.random() * 9 + 1) * 1000);
            HttpSession session = httpServletContext.getSession();
            session.setAttribute(userName, emailCode);
            session.setMaxInactiveInterval(5);
            Map<String, String> map = HardwareInfo.systemAndBrowser(request);
            try {
                MailUtil.sendEmail("Sir/Madam", map.get("browser"), map.get("system"), String.valueOf(emailCode), email);
            } catch (MessagingException e) {
                log.error("=======邮箱验证码发送失败=======", e);
            }
        } else {
            throw new OperationException(OperationError.MAIL_NOT_NULL);
        }

        return Result.fail();
    }

    /**
     * 根据用户名获取用户信息
     *
     * @param username
     * @return
     */
    @Override
    public Result<YunUser> getByUsername(String username) {
        QueryWrapper<YunUser> wrapper = new QueryWrapper<>();
        wrapper.eq("user_name", username);
        YunUser yunUser = yunUserMapper.selectOne(wrapper);
        return Result.success(yunUser);
    }
}
