package com.cybercloud.sprbotfreedom.platform;

import com.github.jeffreyning.mybatisplus.conf.EnableMPP;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;


/**
 * @author liuyutang
 * @date 2023/4/17
 */
@SpringBootApplication(exclude = {MultipartAutoConfiguration.class})
@ComponentScan(basePackages = {"com.cybercloud.sprbotfreedom.**.*"})
@EnableScheduling
@EnableMPP
public class StartApplication {


    public static void main(String[] args) {
        SpringApplication.run(StartApplication.class, args);
    }

}

