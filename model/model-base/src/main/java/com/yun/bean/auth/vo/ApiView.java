package com.yun.bean.auth.vo;

import lombok.Data;

import java.util.List;

/**
 * @program: datacenter
 * @description: 接口视图bean
 * @author: wxf
 * @create: 2019-05-22 14:38
 **/
@Data
public class ApiView extends BaseVo {
    private Long id;
    private String code;
    private String name;
    private String type;
    private String url;
    private String method;
    private List<ParamView> params;
    private String description;
    private String username;
    private String apiType;
}
