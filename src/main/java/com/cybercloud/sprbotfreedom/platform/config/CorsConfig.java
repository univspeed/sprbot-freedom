package com.cybercloud.sprbotfreedom.platform.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoPersistentEntity;
import org.springframework.data.mongodb.core.mapping.MongoPersistentProperty;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 前后端分离解决跨域配置
 * @author liuyutang
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
        /*
         *多个拦截器组成一个拦截器链
         * addPathPatterns 用于添加拦截规则
         * excludePathPatterns 用户排除拦截
         */
		WebMvcConfigurer.super.addInterceptors(registry);
	}

    /**
     * mongdodb 默认不支持 KEY含.的Map。如有需要额外做转化
     */
    @Bean
    public MappingMongoConverter mappingMongoConverter(MongoDatabaseFactory mongoDbFactory, MappingContext<? extends MongoPersistentEntity<?>, MongoPersistentProperty> mappingContext) throws Exception {
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoDbFactory);
        MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, mappingContext);
        converter.setMapKeyDotReplacement("_");
        converter.afterPropertiesSet();
        return converter;
    }
}