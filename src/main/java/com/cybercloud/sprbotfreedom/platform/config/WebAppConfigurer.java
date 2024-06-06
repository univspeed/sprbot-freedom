package com.cybercloud.sprbotfreedom.platform.config;

import jakarta.servlet.MultipartConfigElement;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.MultipartFilter;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * WEB视图解析器
 * @author liuyutang
 */
@EnableWebMvc
@Configuration
public class WebAppConfigurer implements WebMvcConfigurer {
    @Bean
    public InternalResourceViewResolver viewResolver(){

        return new InternalResourceViewResolver();
    }
    @Bean
    public MultipartResolver multipartResolver(){
        return new StandardServletMultipartResolver();
    }

    @Bean
    public MultipartFilter multipartFilter() {
        MultipartFilter multipartFilter = new MultipartFilter();
        multipartFilter.setMultipartResolverBeanName("multipartResolver");
        return multipartFilter;
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.ofMegabytes(1024*1024*500)); // 设置单个文件的最大大小
        factory.setMaxRequestSize(DataSize.ofMegabytes(1024*1024*500)); // 设置一次请求的最大大小
        return factory.createMultipartConfig();
    }
}
