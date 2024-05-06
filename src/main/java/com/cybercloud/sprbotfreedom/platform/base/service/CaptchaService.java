package com.cybercloud.sprbotfreedom.platform.base.service;

import com.cybercloud.sprbotfreedom.platform.base.entity.CaptchaEntity;
import jakarta.servlet.http.HttpServletRequest;


/**
 * 验证码服务
 * @author liuyutang
 * @date 2023/7/7
 */
public interface CaptchaService {
    /**
     * 获取图片验证码Base64字符串
     * @param request
     * @return
     */
    CaptchaEntity getCaptchaImage(HttpServletRequest request);

    /**
     * 校验验证码
     * @param key
     * @param captcha
     * @return
     */
    boolean verify(String key ,String captcha);
}
