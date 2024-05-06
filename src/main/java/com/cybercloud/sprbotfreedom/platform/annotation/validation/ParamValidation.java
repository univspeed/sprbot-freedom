package com.cybercloud.sprbotfreedom.platform.annotation.validation;

import com.cybercloud.sprbotfreedom.platform.enums.aspect.ValidLevel;

import java.lang.annotation.*;

/**
 * 参数校验注解
 * @author liuyutang
 */
@Target({ElementType.METHOD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ParamValidation {
    /**
     * 校验等级
     * @return {{@link ValidLevel}}
     */
    ValidLevel validLevel() default ValidLevel.ALL;

    /**
     * 校验索引，如果校验等级选择ValidLevel.FEW则需要填写此项
     * @return int array
     */
    int[] validIndex() default {};
}
