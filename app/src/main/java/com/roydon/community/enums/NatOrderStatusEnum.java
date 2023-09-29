package com.roydon.community.enums;

/**
 * @author roydon
 * @date 2023/9/29 20:45
 * @description 核酸预约状态
 */
public enum NatOrderStatusEnum {

    ORDERED("0", "已预约"),

    COMPLETED("1", "已完成"),

    CANCELED("2", "已取消");

    private final String code;
    private final String info;

    NatOrderStatusEnum(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public String getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }

    public static NatOrderStatusEnum getByCode(String code) {
        for (NatOrderStatusEnum value : NatOrderStatusEnum.values()) {
            if (code.equals(value.getCode())) {
                return value;
            }
        }
        return null;
    }
}
