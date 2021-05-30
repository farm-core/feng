package com.yun.bean.admin;

import com.yun.bean.base.BasePO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class YunUserApi extends BasePO {

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("接口ID")
    private Long apiId;

    @ApiModelProperty("接口参数id")
    private String paramIds = "[]";

    @ApiModelProperty("接口是否生效")
    private Boolean enabled = true;

    @ApiModelProperty("接口生效时间")
    private Date effectTime;

    @ApiModelProperty("接口过期时间")
    private Date expireTime;

    @ApiModelProperty("版本控制")
    private Integer version;
}
