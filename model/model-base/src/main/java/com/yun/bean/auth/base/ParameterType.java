package com.yun.bean.auth.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: datacenter
 * @description: 参数类型
 * @author: zhanggk
 * @create: 2019-05-24 10:41
 **/
@ApiModel("参数数值类型")
@Data
public class ParameterType {
    @ApiModelProperty("数值类型")
    public static final int NUMBER = 1;
    @ApiModelProperty("时间类型")
    public static final int TIME = 2;
    @ApiModelProperty("字符串类型")
    public static final int STRING = 3;
    @ApiModelProperty("布尔类型")
    public static final int BOOLEAN = 4;
}
