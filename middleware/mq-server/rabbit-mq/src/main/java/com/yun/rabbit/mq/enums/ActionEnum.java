package com.yun.rabbit.mq.enums;

import java.util.Arrays;

@SuppressWarnings("ALL")
public enum ActionEnum {
    /**
     * @Description 创建
     */
    CREATE(1),
    /**
     * @Description 更新
     */
    UPDATE(2),
    /**
     * @Description 删除
     */
    DELETE(3);
    /**
     * @Description action code
     */
    private Integer value;

    ActionEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }


    public static ActionEnum getOne(Integer action) {
        return Arrays.stream(ActionEnum.values()).filter(a -> a.getValue().equals(action)).findFirst().get();
    }
}