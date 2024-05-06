package com.cybercloud.sprbotfreedom.platform.config;

import com.cybercloud.sprbotfreedom.platform.filter.PipelineFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import jakarta.servlet.Filter;


/**
 * 过滤器统一配置
 * @author liuyutang
 */
@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean someFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(pipelineFilter());
        registration.addUrlPatterns("/*");
        registration.setName("pipelineFilter");
        registration.setOrder(1);
        return registration;
    }

    @Bean
    public Filter pipelineFilter(){

        return new PipelineFilter();
    }

}
