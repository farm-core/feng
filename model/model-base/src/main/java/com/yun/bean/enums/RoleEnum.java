package com.yun.bean.enums;

import lombok.Getter;

@Getter
public enum RoleEnum {
    ROLE1(1, "普通用户"),
    ROLE2(10, "部门管理员"),
    ROLE3(99, "超级管理员");
    private Integer value;
    private String roleName;

    RoleEnum(Integer value, String roleName) {
        this.value = value;
        this.roleName = roleName;
    }
}
