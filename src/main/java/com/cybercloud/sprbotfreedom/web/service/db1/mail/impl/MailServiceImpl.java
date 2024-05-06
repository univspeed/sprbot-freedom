package com.cybercloud.sprbotfreedom.web.service.db1.mail.impl;

import com.cybercloud.sprbotfreedom.platform.annotation.log.PrintFunctionLog;
import com.cybercloud.sprbotfreedom.platform.datasource.DB1DataSource;
import com.cybercloud.sprbotfreedom.web.service.db1.mail.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;



/**
 * 邮件服务
 * @author liuyutang
 * @date 2023/8/2
 */
@Slf4j
@Service
@PrintFunctionLog
@DB1DataSource
public class MailServiceImpl implements MailService {

    private final JavaMailSender javaMailSender;

    public MailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public boolean sendMail(String content, String from,String to,String subject,boolean html) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content,html);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            log.error(e.getMessage());
            return false;
        }
        return true;
    }
}
