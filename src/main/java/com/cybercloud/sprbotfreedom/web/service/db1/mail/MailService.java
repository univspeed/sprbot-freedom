package com.cybercloud.sprbotfreedom.web.service.db1.mail;

/**
 * 邮件发送服务
 * @author liuyutang
 * @date 2023/8/2
 */
public interface MailService {
    /**
     * 发送邮件
     * @param content 发送邮件内容
     * @param from 发件人
     * @param to 收件人
     * @param subject 主题
     * @param html 内容是否为html格式
     * @return
     */
    boolean sendMail(String content, String from,String to,String subject,boolean html);
}
