package com.yun.bean.base.context;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.Date;
import java.util.Map;

/**
 * 前台密钥类
 */
@Slf4j
@Data
public class AccessToken {
    private String token;
    private Map<String, Object> information = Collections.emptyMap();
    private Date expiration;
}
