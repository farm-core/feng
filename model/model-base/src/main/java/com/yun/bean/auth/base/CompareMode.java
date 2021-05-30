package com.yun.bean.auth.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("参数比值类型")
@Data
public class CompareMode {
    @ApiModelProperty(value = "IN")
    public static final String IN = "IN";
    @ApiModelProperty(value = "EQUALS")
    public static final String EQ = "EQUALS";
    @ApiModelProperty(value = "GT 大于")
    public static final String GT = "GT";
    @ApiModelProperty(value = "GE 大于等于")
    public static final String GE = "GE";
    @ApiModelProperty(value = "小于")
    public static final String LT = "LT";
    @ApiModelProperty(value = "小于等于")
    public static final String LE = "LE";
    @ApiModelProperty(value = "之间")
    public static final String BETWEEN = "BETWEEN";
}