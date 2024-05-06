package com.cybercloud.sprbotfreedom.platform.base.service.impl;

import com.cybercloud.sprbotfreedom.platform.annotation.log.PrintFunctionLog;
import com.cybercloud.sprbotfreedom.platform.base.entity.CaptchaEntity;
import com.cybercloud.sprbotfreedom.platform.base.service.CaptchaService;
import com.cybercloud.sprbotfreedom.platform.enums.SystemErrorCode;
import com.cybercloud.sprbotfreedom.platform.enums.aspect.PrintLevel;
import com.cybercloud.sprbotfreedom.platform.enums.aspect.PrintLevelRetention;
import com.cybercloud.sprbotfreedom.platform.exception.ServiceException;
import com.cybercloud.sprbotfreedom.platform.util.CacheUtil;
import com.google.code.kaptcha.Producer;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

/**
 * 图片验证码默认实现
 * @author liuyutang
 * @date 2023/7/7
 */
@Service
@PrintFunctionLog(level = PrintLevel.DEBUG,retention = PrintLevelRetention.UP)
public class DefaultCaptchaServiceImpl implements CaptchaService {

    @Value("${system.captcha.captcha-timeout:60}")
    private long captchaTimeOut;
    @Value("${system.captcha.key:captcha}")
    private String captchaName;
    @Autowired
    private Producer defaultKaptcha;

    @Override
    public CaptchaEntity getCaptchaImage(HttpServletRequest request) {
        //获取验证码字符串
        String text = defaultKaptcha.createText();
        //将用户名+时间戳生成唯一标识，验证码为值存储到redis
        String key = request.getSession().getId() + "-" + System.currentTimeMillis();
        CacheUtil.setObject(captchaName,key,text,captchaTimeOut);
        //生成图片
        BufferedImage bufferedImage = defaultKaptcha.createImage(text);
        ByteArrayOutputStream outputStream = null;
        try {
            outputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpg", outputStream);
            String base64 = Base64.getEncoder().encodeToString(outputStream.toByteArray());
            String captchaBase64 = "data:image/jpeg;base64," + base64.replaceAll("\r\n", "");
            return CaptchaEntity.builder()
                    .key(key)
                    .imageBase64(captchaBase64)
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public boolean verify(String key, String captcha) {
        if(StringUtils.isEmpty(key)){
            ServiceException.throwError(SystemErrorCode.ERROR_70000);
        }
        if(StringUtils.isEmpty(captcha)){
            ServiceException.throwError(SystemErrorCode.ERROR_70000);
        }
        String realValue = (String) CacheUtil.getObject(captchaName,key);
        if(StringUtils.isEmpty(realValue)){
            ServiceException.throwError(SystemErrorCode.ERROR_70001);
        }
        if(captcha.equals(realValue)){
            CacheUtil.deleteBoundHash(captchaName,key);
            return true;
        }
        ServiceException.throwError(SystemErrorCode.ERROR_70002);
        return false;
    }
}
