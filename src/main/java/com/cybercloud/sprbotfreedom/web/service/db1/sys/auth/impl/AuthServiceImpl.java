package com.cybercloud.sprbotfreedom.web.service.db1.sys.auth.impl;

import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.SM2;
import com.cybercloud.sprbotfreedom.platform.annotation.log.PrintFunctionLog;
import com.cybercloud.sprbotfreedom.platform.base.entity.UserInfo;
import com.cybercloud.sprbotfreedom.platform.base.service.CaptchaService;
import com.cybercloud.sprbotfreedom.platform.constants.login.LoginConstants;
import com.cybercloud.sprbotfreedom.platform.datasource.annotation.DB1DataSource;
import com.cybercloud.sprbotfreedom.platform.enums.SystemErrorCode;
import com.cybercloud.sprbotfreedom.platform.exception.ServiceException;
import com.cybercloud.sprbotfreedom.platform.util.CacheUtil;
import com.cybercloud.sprbotfreedom.platform.util.JwtUtil;
import com.cybercloud.sprbotfreedom.web.entity.bo.auth.AuthBO;
import com.cybercloud.sprbotfreedom.web.entity.bo.auth.LoginBO;
import com.cybercloud.sprbotfreedom.web.entity.po.db1.sys.resouce.SysResourceEntity;
import com.cybercloud.sprbotfreedom.web.entity.po.db1.sys.role.SysRoleEntity;
import com.cybercloud.sprbotfreedom.web.entity.po.db1.sys.role.SysRoleResourceEntity;
import com.cybercloud.sprbotfreedom.web.entity.po.db1.sys.user.SysRoleUserEntity;
import com.cybercloud.sprbotfreedom.web.entity.po.db1.sys.user.SysUserEntity;
import com.cybercloud.sprbotfreedom.web.service.db1.sys.auth.AuthService;
import com.cybercloud.sprbotfreedom.web.service.db1.sys.resource.SysResourceService;
import com.cybercloud.sprbotfreedom.web.service.db1.sys.role.SysRoleResourceService;
import com.cybercloud.sprbotfreedom.web.service.db1.sys.role.SysRoleService;
import com.cybercloud.sprbotfreedom.web.service.db1.sys.user.SysRoleUserService;
import com.cybercloud.sprbotfreedom.web.service.db1.sys.user.SysUserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 认证服务
 * @author liuyutang
 * @date 2023/7/12
 */
@Service
@PrintFunctionLog
@DB1DataSource
public class AuthServiceImpl implements AuthService {

    @Value("${system.auth-check.open-auth-check}")
    private boolean openAuthCheck;
    @Value("${system.captcha.open-captcha-check}")
    private boolean openCaptchaCheck;
    @Value("${system.sm2.encrypt-private-key}")
    private String privateKey;
    @Value("${system.token.expire-time}")
    private Long tokenExpireTime;
    private final SysUserService sysUserService;
    private final SysRoleService sysRoleService;
    private final SysRoleUserService roleUserService;
    private final CaptchaService captchaService;
    private final SysRoleResourceService sysRoleResourceService;
    private final SysResourceService sysResourceService;

    public AuthServiceImpl(SysUserService sysUserService, SysRoleService sysRoleService, SysRoleUserService roleUserService, CaptchaService captchaService, SysRoleResourceService sysRoleResourceService, SysResourceService sysResourceService) {
        this.sysUserService = sysUserService;
        this.sysRoleService = sysRoleService;
        this.roleUserService = roleUserService;
        this.captchaService = captchaService;
        this.sysRoleResourceService = sysRoleResourceService;
        this.sysResourceService = sysResourceService;
    }

    @Override
    public AuthBO login(LoginBO loginBO) {
        if(!openAuthCheck){
            Map<String,Object> claims = new HashMap<>();
            claims.put("username","unlogin");
            claims.put("id","1");
            String token = JwtUtil.createToken(claims, "1", loginBO.isRemenberMe());
            return AuthBO.builder().token(token).build();
        }
        if(openCaptchaCheck){
            captchaService.verify(loginBO.getKey(),loginBO.getCaptcha());
        }
        String encryptUser = loginBO.getUsername();
        String encryptPassword = loginBO.getPassword();
        SM2 sm2 = SmUtil.sm2(privateKey, null);
        String decryptUser = sm2.decryptStr(encryptUser, KeyType.PrivateKey);
        String decryptPassword = sm2.decryptStr(encryptPassword, KeyType.PrivateKey);

        SysUserEntity byUserName = sysUserService.findByUserName(decryptUser);
        if(byUserName == null){ ServiceException.throwError(SystemErrorCode.ERROR_50004); }

        Long userId = byUserName.getId();
        String operatorName = byUserName.getUsername();
        String sm3Pass = LoginConstants.createSm3Pass(decryptUser, byUserName.getSaltValue(), decryptPassword);
        if(!sm3Pass.equals(byUserName.getPassword())){
            ServiceException.throwError(SystemErrorCode.ERROR_50004);
        }
        List<SysRoleEntity> roles = new ArrayList<>();
        List<SysResourceEntity> perms = new ArrayList<>();
        List<SysRoleUserEntity> byUserId = roleUserService.findByUserId(userId);
        if(CollectionUtils.isNotEmpty(byUserId)){
            Set<Long> roleIds = byUserId.stream().map(SysRoleUserEntity::getRoleId).collect(Collectors.toSet());
            roles.addAll(sysRoleService.defaultFindByIds(roleIds).values());
            List<SysRoleResourceEntity> roleResByRoleId = sysRoleResourceService.findRoleResByRoleId(roleIds.toArray(new Long[0]));
            if(CollectionUtils.isNotEmpty(roleResByRoleId)){
                Map<Long, SysResourceEntity> resources = sysResourceService.defaultFindByIds(roleResByRoleId.stream().map(SysRoleResourceEntity::getResourceId).collect(Collectors.toSet()));
                perms.addAll(resources.values());
            }
        }
        Map<String,Object> userInfo = new HashMap<>();
        userInfo.put("id",userId);
        userInfo.put("username",operatorName);
        userInfo.put("roles",roles);
        userInfo.put("permission",perms);
        String token = JwtUtil.createToken(userInfo, String.valueOf(userId), loginBO.isRemenberMe());
        CacheUtil.set(String.format(LoginConstants.getTokenCacheKey(operatorName)),token,tokenExpireTime);

        return AuthBO.builder()
                .id(userId)
                .username(operatorName)
                .roles(roles)
                .permission(perms)
                .token(token)
                .build();
    }

    @Override
    public boolean logout(String token) {
        if (StringUtils.isNotBlank(token)) {
            UserInfo userInfo = JwtUtil.getUserInfo(token);
            if(userInfo != null){
                return CacheUtil.delete(LoginConstants.getTokenCacheKey(userInfo.getUsername()));
            }
        }
        return false;
    }
}
