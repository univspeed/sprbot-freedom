package com.cybercloud.sprbotfreedom.platform.enums.aspect;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 数据状态枚举类
 * @author liuyutang
 */
@Getter
@AllArgsConstructor
public enum StateEnum {
    /**
     * 无效状态
     */
    INVALID(0,"无效"),
    /**
     * 有效状态
     */
    NORMAL(1,"正常");


    private final int code;
    private final String msg;
}
