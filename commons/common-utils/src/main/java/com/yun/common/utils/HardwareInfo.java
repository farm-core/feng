package com.yun.common.utils;

import nl.bitwalker.useragentutils.Browser;
import nl.bitwalker.useragentutils.OperatingSystem;
import nl.bitwalker.useragentutils.UserAgent;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName HardwareInfo
 * @Description 获取一些硬件信息
 * @Auther wu_xufeng
 * @Date 2020/11/10
 * @Version 1.0
 */
public class HardwareInfo {

    public static Map<String, String> systemAndBrowser(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();
        //获取浏览器信息
        String ua = request.getHeader("User-Agent");
        //转成UserAgent对象
        UserAgent userAgent = UserAgent.parseUserAgentString(ua);
        //获取浏览器信息
        Browser browser = userAgent.getBrowser();
        //获取系统信息
        OperatingSystem os = userAgent.getOperatingSystem();
        //系统名称
        String system = os.getName();
        //浏览器名称
        String browserName = browser.getName();
        map.put("system", system);
        map.put("browser", browserName);
        return map;
    }
}
