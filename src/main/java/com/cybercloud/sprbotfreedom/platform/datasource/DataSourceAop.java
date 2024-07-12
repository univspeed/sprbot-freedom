package com.cybercloud.sprbotfreedom.platform.datasource;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * 动态数据源AOP切入点、用于获取动态数据源类型
 * @author liuyutang
 */
@Slf4j
@Aspect
@Component
public class DataSourceAop {

    @Around("@within(com.cybercloud.sprbotfreedom.platform.datasource.annotation.DB1DataSource)")
    public Object setDataSource2Db1(ProceedingJoinPoint pjp) throws Throwable {
        DataSourceType.setDataBaseType(DataSourceType.DataBaseType.DB1);
        Object proceed = pjp.proceed();
        DataSourceType.clearDataBaseTypew();
        return proceed;
    }

    @Around("@within(com.cybercloud.sprbotfreedom.platform.datasource.annotation.DB2DataSource)")
    public Object setDataSource2Db2(ProceedingJoinPoint pjp) throws Throwable {
        DataSourceType.setDataBaseType(DataSourceType.DataBaseType.DB2);
        Object proceed = pjp.proceed();
        DataSourceType.clearDataBaseTypew();
        return proceed;
    }

    @Around("@within(com.cybercloud.sprbotfreedom.platform.datasource.annotation.DB3DataSource)")
    public Object setDataSource2Db3(ProceedingJoinPoint pjp) throws Throwable {
        DataSourceType.setDataBaseType(DataSourceType.DataBaseType.DB3);
        Object proceed = pjp.proceed();
        DataSourceType.clearDataBaseTypew();
        return proceed;
    }

}
