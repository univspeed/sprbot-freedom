package com.cybercloud.sprbotfreedom.platform.datasource;


import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.cybercloud.sprbotfreedom.platform.interceptors.MybatisInterceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 动态数据源配置类
 * @author liuyutang
 */
@Configuration
@MapperScan(basePackages = "com.cybercloud.sprbotfreedom.*.**.dao.**.*",sqlSessionFactoryRef = "sqlSessionFactory")
public class DataSourceConfig {

    private final MybatisInterceptor mybatisInterceptor;

    @Value("${mybatis.mapper-locations:mappers/**/*.xml}")
    private String mapperLocations;

    public DataSourceConfig(MybatisInterceptor mybatisInterceptor) {
        this.mybatisInterceptor = mybatisInterceptor;
    }

    @Primary
    @Bean(name = "db1DataSource")
    @ConfigurationProperties(prefix = "spring.datasource.druid.db1")
    public DataSource getDb1DataSource(){
        return DruidDataSourceBuilder.create().build();
    }

    @Bean(name = "db2DataSource")
    @ConditionalOnProperty(name ="spring.datasource.druid.db2",havingValue = "com.mysql.cj.jdbc.Driver")
    @ConfigurationProperties(prefix = "spring.datasource.druid.db2")
    public DataSource getDb2DataSource(){
        return DruidDataSourceBuilder.create().build();
    }

    @Bean(name = "db3DataSource")
    @ConditionalOnProperty(name ="spring.datasource.druid.db3",havingValue = "com.mysql.cj.jdbc.Driver")
    @ConfigurationProperties(prefix = "spring.datasource.druid.db3")
    public DataSource getDb3DataSource(){
        return DruidDataSourceBuilder.create().build();
    }


    @Bean(name = "dynamicDataSource")
    public DynamicDataSource dataSource(
            @Qualifier("db1DataSource") Optional<DataSource> db1DataSource,
            @Qualifier("db2DataSource")Optional<DataSource> db2DataSource,
            @Qualifier("db3DataSource")Optional<DataSource> db3DataSource){
        Map<Object, Object> targetDataSource = new HashMap<>();
        db1DataSource.ifPresent(dataSource -> targetDataSource.put(DataSourceType.DataBaseType.DB1, dataSource));
        db2DataSource.ifPresent(dataSource -> targetDataSource.put(DataSourceType.DataBaseType.DB2, dataSource));
        db3DataSource.ifPresent(dataSource -> targetDataSource.put(DataSourceType.DataBaseType.DB3, dataSource));
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        dynamicDataSource.setTargetDataSources(targetDataSource);
        dynamicDataSource.setDefaultTargetDataSource(db1DataSource.orElse(null));
        return dynamicDataSource;
    }

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dynamicDataSource") DataSource dynamicDataSource,@Qualifier("mybatisPlusInterceptor") MybatisPlusInterceptor mybatisPlusInterceptor) throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dynamicDataSource);
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(mapperLocations));
        sqlSessionFactoryBean.setPlugins(mybatisInterceptor,mybatisPlusInterceptor);
        return sqlSessionFactoryBean.getObject();
    }
}
