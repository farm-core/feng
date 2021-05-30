package com.yun.bean.admin;

import com.yun.bean.base.BasePO;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class YunApi extends BasePO {
    /**
     * 接口编码
     */
    private String code;
    /**
     * 接口名称
     */
    private String name;
    /**
     * 接口请求类型
     */
    private String type;
    /**
     * 接口请求路径
     */
    private String url;
    /**
     * 接口所属APP
     */
    private String app;
    /**
     * 接口参数
     */
    private String parameters;
    /**
     * 接口描述
     */
    private String description;
}

