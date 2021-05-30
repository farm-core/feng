package com.yun.common.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IpUtils {

    private static String isOutside(HttpServletRequest request) {
        Boolean result = false;
        // 获取客户端IP地址，考虑反向代理的问题
        String ip = request.getHeader("x-forwarded-for");
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if ("127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip)) {
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                    ip = inet.getHostAddress();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        }
        if (!StringUtils.isEmpty(ip) && ip.length() > 15) {
            ip = ip.substring(0, ip.indexOf(","));
        }
        /*
         *  判断客户单IP地址是否为内网地址
         *  内网IP网段：
         *  10.0.0.0-10.255.255.255
         *  172.16.0.0-172.31.255.255
         *  192.168.0.0-192.168.255.255
         */
        String reg = "^(192\\.168|172\\.(1[6-9]|2\\d|3[0,1]))(\\.(2[0-4]\\d|25[0-5]|[0,1]?\\d?\\d)){2}$|^10(\\.([2][0-4]\\d|25[0-5]|[0,1]?\\d?\\d)){3}$";
        Pattern p = Pattern.compile(reg);
        Matcher matcher = p.matcher(ip);
        result = matcher.find();
        return !result == true ? ip : null;
    }

    public static String isOutside2(ServerHttpRequest request) {
        boolean result = false;
        HttpHeaders headers = request.getHeaders();
        // 获取客户端IP地址，考虑反向代理的问题
        String ip = headers.getFirst("x-forwarded-for");
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("WL-Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddress().getAddress().getHostAddress();
            if ("127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip)) {
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                    ip = inet.getHostAddress();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        }
        if (!StringUtils.isEmpty(ip) && ip.length() > 15) {
            ip = ip.substring(0, ip.indexOf(","));
        }
        /*
         *  判断客户单IP地址是否为内网地址
         *  内网IP网段：
         *  10.0.0.0-10.255.255.255
         *  172.16.0.0-172.31.255.255
         *  192.168.0.0-192.168.255.255
         */
//        String reg = "^(192\\.168|172\\.(1[6-9]|2\\d|3[0,1]))(\\.(2[0-4]\\d|25[0-5]|[0,1]?\\d?\\d)){2}$|^10(\\.([2][0-4]\\d|25[0-5]|[0,1]?\\d?\\d)){3}$";
//        Pattern p = Pattern.compile(reg);
//        Matcher matcher = p.matcher(ip);
//        result = matcher.find();
//        return result ? null : ip;
        return ip;
    }

}
