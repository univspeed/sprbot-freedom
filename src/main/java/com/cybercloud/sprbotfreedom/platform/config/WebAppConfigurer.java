package com.cybercloud.sprbotfreedom.platform.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
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
}
