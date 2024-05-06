package com.cybercloud.sprbotfreedom.web.controller.center.mail;

import com.cybercloud.sprbotfreedom.web.service.db1.mail.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 邮件预警
 * @author liuyutang
 * @date 2023/7/17
 */
@RestController
@RequestMapping("/api/v1/mail")
public class SendMailController {

    @Autowired
    private MailService mailService;

    @GetMapping("send")
    public boolean send(){

        return true;
    }
}
