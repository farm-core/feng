package com.yun.bean.admin;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yun.bean.base.BasePO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * (YunUser)表实体类
 *
 * @author makejava
 * @since 2020-11-05 16:36:38
 */
@Data
@ToString
@NoArgsConstructor
@EqualsAndHashCode()
@TableName("yun_user")
@SuppressWarnings("serial")
@ApiModel(value = "用户信息实体")
public class YunUser extends BasePO {

    /**
     * 用户昵称
     */
    @ApiModelProperty(value = "用户昵称")
    private String name;

    /**
     * 用户性别
     */
    @ApiModelProperty(value = "用户性别")
    private String userSex;

    /**
     * 用户头像
     */
    @ApiModelProperty(value = "用户头像")
    private String userImg;

    /**
     * 用户登陆名
     */
    @ApiModelProperty(value = "用户登陆名", required = true, example = "zhangsan")
    @NotBlank(message = "用户登陆名称不允许为空,请输入")
    private String userName;

    /**
     * 用户登陆密码
     */
    @ApiModelProperty(value = "用户登陆密码", required = true, example = "zhangsan")
    @Pattern(regexp = "^[a-zA-Z]\\w{5,17}$", message = "以字母开头，长度在6~18之间，只能包含字符、数字和下划线")
    @NotBlank(message = "用户登陆密码不允许为空,请输入")
    private String password;

    /**
     * 用户类型
     */
    @ApiModelProperty(value = "用户类型")
    private String userType;

    /**
     * 用户邮箱
     */
    @Email(message = "邮箱格式不正确")
    @ApiModelProperty(value = "用户邮箱")
    private String userMail;

    /**
     * 邮箱验证码
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "邮箱验证码")
    private String emailNum;

    /**
     * 用户手机号
     */
    @ApiModelProperty(value = "用户手机号")
    private Integer phoneNumber;

    /**
     * 部门ID
     */
    @ApiModelProperty(value = "部门ID")
    private Integer groupId;

    /**
     * 菜单ID
     */
    @ApiModelProperty(value = "菜单ID")
    private Integer menuId;

    /**
     * 角色ID
     */
    @ApiModelProperty(value = "角色ID")
    private Integer roleId;

    /**
     * 验证码
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "验证码")
    private String verifyCode;

    /**
     * 用户是否生效
     */
    @ApiModelProperty(value = "用户是否生效")
    private Boolean enabled;

    /**
     * 账户是否过期
     */
    @ApiModelProperty(value = "账户是否过期")
    private Boolean accountNonExpired;

    /**
     * 账户凭证是否过期
     */
    @ApiModelProperty(value = "账户凭证是否过期")
    private Boolean credentialsNonExpired;

    /**
     * 账号是否锁定
     */
    @ApiModelProperty(value = "账号是否锁定")
    private Boolean accountNonLocked;

    /**
     * 账号可用时长（默认30天）
     */
    @ApiModelProperty(value = "账号可用时长（默认30天）")
    private String loginNameTime;

    /**
     * 账号可用时长（默认15天）
     */
    @ApiModelProperty(value = "账号可用时长（默认15天）")
    private String loginPasswordTime;

    /**
     * 最后登陆时间
     */
    @ApiModelProperty(value = "最后登陆时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date loginDate;

    /**
     * 最后ip
     */
    @ApiModelProperty(value = "最后ip")
    private String loginIp;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private Date remark;

    /**
     * 数据版本号
     */
    @Version
    @ApiModelProperty(value = "数据版本号")
    private Integer version = 1;
}