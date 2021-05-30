package com.yun.bean.auth.vo;

import lombok.Data;

import java.util.Date;

/**
 * @program:
 * @description: 用户信息视图
 * @author: wxf
 * @create: 2020-06-03 14:30
 **/
@Data
public class Userview extends BaseVo {
    // 用户ID
    private Long id;
    //用户名称
    private String name;
    //用户对应项目id
    private Long projectId;
    //用户联系手机号码
    private String mobile;
    //用户账号
    private String username;
    //用户密码
//    private String password;
    //用户服务器地址
    private String[] ipAddress;
    //是否生效
    private Boolean enabled;
    //是否过期
    private Boolean accountNonExpired;
    //密码是否过期
    private Boolean credentialsNonExpired;
    //账号是否锁定
    private Boolean accountNonLocked;
    //账号生效时间
    private Date effectedTime;
    //账号失效时间
    private Date unEffectedTime;
    //数据版本号
    private Integer version;
}
