package com.cybercloud.sprbotfreedom.platform.base.entity;

import lombok.Builder;
import lombok.Data;

/**
 * 图片验证码实体类
 * @author liuyutang
 * @date 2023/7/7
 */
@Data
@Builder
public class CaptchaEntity {
    /**
     * 获取验证码口令
     */
    private String key;
    /**
     * 图片验证码内容base64加密
     */
    private String imageBase64;
}
