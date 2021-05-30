package com.yun.bean.base;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Date;

/**
 * 基础数据VO
 */
@Data
@Slf4j
public class BaseVO implements Serializable {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty("用户ID,自动不用填写")
    private Integer id;
    @ApiModelProperty(value = "创建者")
    private String createdBy;
    @ApiModelProperty(value = "更新者")
    private String updatedBy;
    @ApiModelProperty(value = "创建时间")
    private Date createdTime;
    @ApiModelProperty(value = "更新时间")
    private Date updatedTime;
    @ApiModelProperty(value = "是否删除", example = "1")
    private Integer deleted;
}
