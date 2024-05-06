package com.cybercloud.sprbotfreedom.platform.enums.aspect;

/**
 * 日志打印级别保留策略枚举
 * @author liuyutang
 */
public enum PrintLevelRetention {
    /**
     * 向上兼容打印
     * 例如：如果当前等级为DEBUG，则打印标记为 DEBUG，INFO，WARN，ERROR 的等级的日志。
     */
    UP,
    /**
     * 只打印当前级别
     * 例如：如果当前等级为DEBUG，则只打印标记为DEBUG级别的日志。
     */
    ONLY,
    /**
     * 自由配置打印
     * 当指定此项时，要配置一个打印级别数组，当日志级别在此数组中则打印。
     */
    FREE
}
