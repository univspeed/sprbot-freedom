package com.cybercloud.sprbotfreedom.platform.base.controller;

import com.cybercloud.sprbotfreedom.platform.annotation.log.PrintFunctionLog;
import com.cybercloud.sprbotfreedom.platform.base.entity.CaptchaEntity;
import com.cybercloud.sprbotfreedom.platform.base.service.CaptchaService;
import com.cybercloud.sprbotfreedom.platform.enums.aspect.PrintLevel;
import com.cybercloud.sprbotfreedom.platform.enums.aspect.PrintLevelRetention;
import io.swagger.annotations.Api;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * 图片验证码校验
 * @author liuyutang
 * @date 2023/7/7
 */
@Slf4j
@Api(value="图片验证码校验",tags={"图片验证码校验"})
@RestController
@RequestMapping("/api/v1/captcha")
@Validated
@PrintFunctionLog(level = PrintLevel.DEBUG,retention = PrintLevelRetention.UP)
public class CaptchaController extends BaseController{

    @Autowired
    private CaptchaService captchaService;

    /**
     * 获取验证码
     */
    @GetMapping("/image")
    @ResponseBody
    public CaptchaEntity getImage(HttpServletRequest request) {
        return captchaService.getCaptchaImage(request);
    }

    /**
     * 校验验证码
     * @return
     */
    @GetMapping( "/verify")
    public boolean verify() {

        return captchaService.verify(getPara("key"),getPara("value"));
    }
}
