package com.cybercloud.sprbotfreedom.platform.datasource.annotation;

import java.lang.annotation.*;

/**
 * 数据源1
 * @author liuyutang
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DB1DataSource {
}
