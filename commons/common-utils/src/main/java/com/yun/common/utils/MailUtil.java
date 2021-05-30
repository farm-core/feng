package com.yun.common.utils;

import com.sun.mail.util.MailSSLSocketFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 邮箱发送
 */
@Slf4j
public class MailUtil {


    private static String content = "Hey loginName!\n" +
            "\n" +
            "A sign in attempt requires further verification because we did not recognize your device. To complete the sign in, enter the verification code on the unrecognized device.\n" +
            "TimeLimit：5 minute,\n" +
            "Device: browser on system\n" +
            "Verification code: <u>code<u/>";

    /**
     * @param loginName  登陆/注册用户名
     * @param browser    浏览器
     * @param system     操作系统
     * @param code       验证码
     * @param recipients 发送人
     * @throws MessagingException
     */
    public static void sendEmail(String loginName, String browser, String system, String code, String recipients) throws MessagingException {
        content = content.replaceAll("loginName", loginName)
                .replaceAll("browser", browser)
                .replaceAll("system", system)
                .replaceAll("code", code);
        sendMail(content, recipients);
    }


    /**
     * 163邮箱
     */
    // 发件人 账号和密码
    public static final String MY_EMAIL_ACCOUNT = "wuxufeng0802@163.com";
    public static final String MY_EMAIL_PASSWORD = "JRITCFEQNRUFJWWN";// 密码,是你自己的设置的授权码

    // SMTP服务器(这里用的163 SMTP服务器)
    public static final String MEAIL_163_SMTP_HOST = "smtp.163.com";
    public static final String SMTP_163_PORT = "25";// 端口号,这个是163使用到的;QQ的应该是465或者875

    // 收件人
    public static final String RECEIVE_EMAIL_ACCOUNT = "";

    private static void sendMail(String content, String recipients) throws MessagingException {
        Properties p = new Properties();
        p.setProperty("mail.smtp.host", MEAIL_163_SMTP_HOST);
        p.setProperty("mail.smtp.port", SMTP_163_PORT);
        p.setProperty("mail.smtp.socketFactory.port", SMTP_163_PORT);
        p.setProperty("mail.smtp.auth", "true");
        p.setProperty("mail.smtp.socketFactory.class", "SSL_FACTORY");

        Session session = Session.getInstance(p, new Authenticator() {
            // 设置认证账户信息
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(MY_EMAIL_ACCOUNT, MY_EMAIL_PASSWORD);
            }
        });
        session.setDebug(true);
        MimeMessage message = new MimeMessage(session);
        // 发件人
        message.setFrom(new InternetAddress(MY_EMAIL_ACCOUNT));
        // 收件人和抄送人
        // 单发
        message.setRecipients(Message.RecipientType.TO, recipients);
        // 群发
//        message.setRecipients(Message.RecipientType.CC, MY_EMAIL_ACCOUNT);

        // 内容(这个内容还不能乱写,有可能会被SMTP拒绝掉;多试几次吧)
        message.setSubject("云朵服务系统");
        message.setContent("<h3>" + content + "</h3>", "text/html;charset=UTF-8");
        message.setSentDate(new Date());
        message.saveChanges();
        Transport.send(message);
    }


    /**
     * 腾讯企业邮箱
     */
    private static String account = "zengxw@3clear.com";// 登录账户
    private static String password = "Wei15382416735$";// 登录密码
    private static String host = "smtp.exmail.qq.com";// 服务器地址
    private static String port = "465";
    private static String protocol = "smtp";// 协议

    //初始化参数
    public static Session initProperties() {
        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", protocol);
        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.smtp.port", port);
        // 使用smtp身份验证
        properties.put("mail.smtp.auth", "true");
        // 使用SSL,企业邮箱必需 start
        // 开启安全协议
        MailSSLSocketFactory mailSSLSocketFactory = null;
        try {
            mailSSLSocketFactory = new MailSSLSocketFactory();
            mailSSLSocketFactory.setTrustAllHosts(true);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        properties.put("mail.smtp.enable", "true");
        properties.put("mail.smtp.ssl.socketFactory", mailSSLSocketFactory);
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.socketFactory.fallback", "false");
        properties.put("mail.smtp.socketFactory.port", port);
        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(account, password);
            }
        });
        // 使用SSL,企业邮箱必需 end
        // TODO 显示debug信息 正式环境注释掉
//        session.setDebug(true);
        return session;
    }

    /**
     * @param sender       发件人别名
     * @param subject      邮件主题
     * @param content      邮件内容
     * @param receiverList 接收者列表,多个接收者之间用","隔开
     * @param fileSrc      附件地址
     */
    public static void send(String sender, String subject, String content, String receiverList, String fileSrc) {
        try {
            Session session = initProperties();
            MimeMessage mimeMessage = new MimeMessage(session);
            // 发件人,可以设置发件人的别名
            mimeMessage.setFrom(new InternetAddress(account, sender));
            // 收件人,多人接收
            InternetAddress[] internetAddressTo = InternetAddress.parse(receiverList);
            mimeMessage.setRecipients(Message.RecipientType.TO, internetAddressTo);
            // 主题
            mimeMessage.setSubject(subject);
            // 时间
            mimeMessage.setSentDate(new Date());
            // 容器类 附件
            MimeMultipart mimeMultipart = new MimeMultipart();
            // 可以包装文本,图片,附件
            MimeBodyPart bodyPart = new MimeBodyPart();
            // 设置内容
            bodyPart.setContent(content, "text/html; charset=UTF-8");
            mimeMultipart.addBodyPart(bodyPart);
            // 添加图片&附件
            if (StringUtils.isNotEmpty(fileSrc)) {
                bodyPart = new MimeBodyPart();
                bodyPart.attachFile(fileSrc);
                mimeMultipart.addBodyPart(bodyPart);
            }
            mimeMessage.setContent(mimeMultipart);
            mimeMessage.saveChanges();
            Transport.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //判断Email合法性
    public static boolean isEmail(String email) {
        if (StringUtils.isEmpty(email)) {
            return false;
        }
        String rule = "[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?";
        Pattern pattern;
        Matcher matcher;
        pattern = Pattern.compile(rule);
        matcher = pattern.matcher(email);
        if (matcher.matches()) {
            return true;
        } else {
            return false;
        }
    }

    public static void main(String[] args) {
        try {
            sendMail("Hey feng-yun77!\n\r" +
                    "\n" +
                    "A sign in attempt requires further verification because we did not recognize your device. To complete the sign in, enter the verification code on the unrecognized device.\n" +
                    "\n" +
                    "Device: Chrome on Windows\n" +
                    "Verification code: <u>064455</u>", "邮件地址");
        } catch (MessagingException e) {
            log.error("发送邮件失败", e);
        }
//        send("中科三清信息中心", "数据警报",
//                "2020-02-01 12:00:00 到 2020-02-01 13:00:00之间有半个小时一上的数据中断", "接收人邮箱地址（可单人可多人）", "附件地址");
    }
}
