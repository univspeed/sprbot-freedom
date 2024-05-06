package com.cybercloud.sprbotfreedom.platform.enums.aspect;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 日志打印登记
 * @author liuyutang 
 */
@Getter
@AllArgsConstructor
public enum PrintLevel {
    /**
     * TRACE级别日志
     */
    TRACE(0),
    /**
     * DEBUG级别日志
     */
    DEBUG(1),
    /**
     * INFO级别日志
     */
    INFO(2),
    /**
     * WARN级别日志
     */
    WARN(3),
    /**
     * ERROR级别日志
     */
    ERROR(4);

    private final int height;

    /**
     * 根据忽略大小写名称获取枚举
     * @param name 枚举项名
     * @return {{@link PrintLevel}}
     */
    public static PrintLevel getByName(String name){
        if(name == null){return null;}
        PrintLevel[] values = PrintLevel.values();
        for (PrintLevel value : values) {
            if(name.equalsIgnoreCase(value.name())){
                return value;
            }
        }
        return null;
    }
}
