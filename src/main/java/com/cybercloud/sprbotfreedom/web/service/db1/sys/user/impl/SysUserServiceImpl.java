package com.cybercloud.sprbotfreedom.web.service.db1.sys.user.impl;

import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.SM2;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cybercloud.sprbotfreedom.platform.annotation.log.PrintFunctionLog;
import com.cybercloud.sprbotfreedom.platform.base.entity.PageResult;
import com.cybercloud.sprbotfreedom.platform.base.service.impl.BaseServiceImpl;
import com.cybercloud.sprbotfreedom.platform.constants.login.LoginConstants;
import com.cybercloud.sprbotfreedom.platform.datasource.annotation.DB1DataSource;
import com.cybercloud.sprbotfreedom.platform.enums.SystemErrorCode;
import com.cybercloud.sprbotfreedom.platform.exception.ServiceException;
import com.cybercloud.sprbotfreedom.platform.util.DateUtil;
import com.cybercloud.sprbotfreedom.platform.util.genid.UUIDUtil;
import com.cybercloud.sprbotfreedom.web.dao.db1.sys.user.SysUserDAO;
import com.cybercloud.sprbotfreedom.web.entity.bo.user.ModifyPassBO;
import com.cybercloud.sprbotfreedom.web.entity.bo.user.SaveUserBO;
import com.cybercloud.sprbotfreedom.web.entity.po.db1.sys.user.SysUserEntity;
import com.cybercloud.sprbotfreedom.web.service.db1.sys.user.SysRoleUserService;
import com.cybercloud.sprbotfreedom.web.service.db1.sys.user.SysUserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

/**
 * 操作员业务层接口实现
 * @author liuyutang
 * @date 2023/7/11
 */
@Service
@PrintFunctionLog
@DB1DataSource
public class SysUserServiceImpl extends BaseServiceImpl<SysUserEntity, SysUserDAO> implements SysUserService {

    @Value("${system.sm2.encrypt-private-key}")
    private String privateKey;
    private final SysRoleUserService roleUserService;

    public SysUserServiceImpl(SysRoleUserService roleUserService) {
        this.roleUserService = roleUserService;
    }

    @Override
    public PageResult<SysUserEntity> page(int page, int pageSize, String name) {
        Page<SysUserEntity> sysUserEntityPage = dao.selectPage(getEmptyPage(page, pageSize),
                normalStateLambdaQueryWrapper().lambda()
                        .eq(StringUtils.isNotBlank(name), SysUserEntity::getUsername, name)
        );
        return PageResult.create(sysUserEntityPage);
    }

    @Override
    public List<SysUserEntity> findAll(String name) {
        return dao.selectList(
                normalStateLambdaQueryWrapper().lambda()
                    .eq(StringUtils.isNotBlank(name), SysUserEntity::getUsername, name)
        );
    }

    @Override
    public SysUserEntity findById(Long id) {
        return defaultFindOne(id);
    }

    @Override
    public SysUserEntity add(SaveUserBO user) {
        SM2 sm2 = SmUtil.sm2(privateKey, null);
        String userName = sm2.decryptStr(user.getUsername(), KeyType.PrivateKey);
        SysUserEntity sysUserEntity = findByUserName(userName);
        Assert.isNull(sysUserEntity,"已存在此用户，请重新注册");
        Assert.notNull(user.getSavePass(),"保存用户密码不能为空");
        SysUserEntity finalUser = saveInfo(user,true,e -> {
            String salt = UUIDUtil.genUUID();
            e.setSaltValue(salt);
            String password = user.getSavePass();
            String decoderPass = sm2.decryptStr(password, KeyType.PrivateKey);
            String finalPass = LoginConstants.createSm3Pass(e.getUsername(), salt, decoderPass);
            e.setPassword(finalPass);
            return e;
        });
        user.setSavePass(null);
        SysUserEntity saveResult = defaultSave(finalUser, true);
        if(saveResult != null){
            roleUserService.bindUserAndRole(user.getId(),user.getRoles());
        }
        return saveResult;
    }

    @Override
    public SysUserEntity update(SaveUserBO old) {
        SysUserEntity sysUserEntity = null;
        try {
            sysUserEntity = defaultFindOne(old.getId());
            Assert.notNull(sysUserEntity, "修改用户不存在");
            sysUserEntity.setComment(old.getComment());
            sysUserEntity.setVerifyCode(old.getVerifyCode());
            sysUserEntity.setEmail(old.getEmail());
            sysUserEntity = defaultUpdate(sysUserEntity);
            if(sysUserEntity != null){
                roleUserService.bindUserAndRole(sysUserEntity.getId(),old.getRoles());
            }
        }catch (IllegalArgumentException e){
            throw e;
        }
        return sysUserEntity;
    }

    @Override
    public boolean del(Long id) {
        return CollectionUtils.isNotEmpty(defaultLogicDelete(id));
    }

    @Override
    public boolean modifyPassword(ModifyPassBO modifyPassBO) {
        SM2 sm2 = SmUtil.sm2(privateKey, null);
        Long id = modifyPassBO.getId();
        SysUserEntity byId = findById(id);
        Assert.notNull(byId,"修改密码用户不存在");
        String saltValue = byId.getSaltValue();
        String password = byId.getPassword();
        String operatorName = byId.getUsername();

        String oldPass = modifyPassBO.getOldPass();
        String newPass = modifyPassBO.getNewPass();
        String newConfirmPass = modifyPassBO.getComfirmPass();
        String decoderOldPass = sm2.decryptStr(oldPass, KeyType.PrivateKey);
        String decoderNewPass = sm2.decryptStr(newPass, KeyType.PrivateKey);
        String decoderConfirmPass = sm2.decryptStr(newConfirmPass, KeyType.PrivateKey);

        if(!decoderNewPass.equals(decoderConfirmPass)){
            ServiceException.throwError(SystemErrorCode.ERROR_50002);
        }

        String sm3Pass = LoginConstants.createSm3Pass(operatorName, saltValue, decoderOldPass);
        if(!sm3Pass.equals(password)){
            ServiceException.throwError(SystemErrorCode.ERROR_50001);
        }

        String sm3NewPass = LoginConstants.createSm3Pass(operatorName, saltValue, decoderNewPass);
        if(sm3NewPass.equals(password)){
            ServiceException.throwError(SystemErrorCode.ERROR_50006);
        }

        String newSalt = UUIDUtil.genUUID();
        byId.setSaltValue(newSalt);
        byId.setPassword(LoginConstants.createSm3Pass(operatorName, newSalt, decoderNewPass));
        byId.setLastPswTime(DateUtil.getDateTime());
        return defaultUpdate(byId) != null;
    }

    @Override
    public boolean modifyLastLoginTime(Long userId, String time) {
        SysUserEntity byId = findById(userId);
        if(byId == null){
            Assert.notNull(byId,"修改密码用户不存在");
        }
        byId.setLastLoginTime(time);
        defaultUpdate(byId);
        return false;
    }

    @Override
    public SysUserEntity findByUserName(String username) {
        if (StringUtils.isNotBlank(username)) {
            return dao.selectOne(normalStateLambdaQueryWrapper().lambda().eq(SysUserEntity::getUsername, username));
        }
        return null;
    }

}
