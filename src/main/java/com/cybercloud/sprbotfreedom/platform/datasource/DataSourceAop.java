package com.cybercloud.sprbotfreedom.platform.datasource;

import lombok.extern.slf4j.Slf4j;
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

    @Before("@within(com.cybercloud.sprbotfreedom.platform.datasource.DB1DataSource)")
    public void setDataSource2Db1(){
        DataSourceType.setDataBaseType(DataSourceType.DataBaseType.DB1);
    }

    @Before("@within(com.cybercloud.sprbotfreedom.platform.datasource.DB2DataSource)")
    public void setDataSource2Db2(){
        DataSourceType.setDataBaseType(DataSourceType.DataBaseType.DB2);
    }

    @Before("@within(com.cybercloud.sprbotfreedom.platform.datasource.DB3DataSource)")
    public void setDataSource2Db3(){
        DataSourceType.setDataBaseType(DataSourceType.DataBaseType.DB3);
    }

}
