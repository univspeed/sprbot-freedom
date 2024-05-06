package com.cybercloud.sprbotfreedom.web.controller.sys.auth;

import com.cybercloud.sprbotfreedom.platform.base.controller.BaseController;
import com.cybercloud.sprbotfreedom.web.entity.bo.auth.AuthBO;
import com.cybercloud.sprbotfreedom.web.entity.bo.auth.LoginBO;
import com.cybercloud.sprbotfreedom.web.service.db1.sys.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liuyutang
 * @date 2023/7/12
 */
@RestController
@RequestMapping("/api/v1")
public class AuthController extends BaseController {

    @Value("${system.token.header}")
    private String tokenName;
    @Value("${system.auth-check.open-auth-check}")
    private boolean openCaptchaCheck;
    @Value("${system.captcha.open-captcha-check}")
    private boolean openAuthCheck;

    @Autowired
    private AuthService authService;

    @PostMapping("login")
    public AuthBO login(@RequestBody LoginBO loginBO){
        Assert.notNull(loginBO,"登录信息不能为空");
        if(openCaptchaCheck){
            Assert.notNull(loginBO.getKey(),"验证码校验key不能为空");
            Assert.notNull(loginBO.getCaptcha(),"验证码不能为空");
        }
        if(openAuthCheck){
            Assert.notNull(loginBO.getUsername(),"用户名不能为空");
            Assert.notNull(loginBO.getCaptcha(),"密码不能为空");
        }
        return authService.login(loginBO);
    }

    @PostMapping("logout")
    public boolean logout(){
        String token = getHeader(tokenName);
        Assert.notNull(token,"退出失败");
        return authService.logout(token);
    }
}
