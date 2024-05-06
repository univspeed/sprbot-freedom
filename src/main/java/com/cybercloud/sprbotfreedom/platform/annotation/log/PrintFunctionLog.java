package com.cybercloud.sprbotfreedom.platform.annotation.log;

import com.cybercloud.sprbotfreedom.platform.enums.aspect.PrintLevel;
import com.cybercloud.sprbotfreedom.platform.enums.aspect.PrintLevelRetention;

import java.lang.annotation.*;

/**
 * 接口参数打印注解
 * @author liuyutang
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PrintFunctionLog {
    /**
     * 打印级别
     * @return PrintLevel
     */
    PrintLevel level() default PrintLevel.DEBUG;

    /**
     * 日志打印级别保留策略
     * @return PrintLevelRetention
     */
    PrintLevelRetention retention() default PrintLevelRetention.UP;

    /**
     * 日志打印级别数组，仅提供给FREE策略使用
     * @return PrintLevel[]
     */
    PrintLevel[] levels() default {};
}
