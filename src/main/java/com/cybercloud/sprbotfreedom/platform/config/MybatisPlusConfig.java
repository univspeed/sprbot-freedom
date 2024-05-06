package com.cybercloud.sprbotfreedom.platform.config;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.cybercloud.sprbotfreedom.platform.interceptors.MybatisInterceptor;
import com.cybercloud.sprbotfreedom.platform.util.genid.MybatisPlusIdGenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Mybatis Plus 插件配置
 * @author liuyutang
 */
@Configuration
@EnableTransactionManagement
public class MybatisPlusConfig  {

    @Autowired
    private DataSource dataSource;

    /**
     * 新增拦截器，用于将数据库类型动态传入到Mapper文件中
     * @return
     */
    @Bean
    public MybatisInterceptor newInterceptor(){
        String databaseProductName;
        try {
            Connection connection = dataSource.getConnection();
            databaseProductName = connection.getMetaData().getDatabaseProductName();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new MybatisInterceptor(databaseProductName);
    }

    /**
     * 创建雪花算法生成器
     * @return
     */
    @Bean
    public IdentifierGenerator createIdentifierGenerator(){
        return new MybatisPlusIdGenUtil();
    }

}
