package com.cybercloud.sprbotfreedom.web.service.db1.sys.user;

import com.cybercloud.sprbotfreedom.platform.annotation.log.PrintFunctionLog;
import com.cybercloud.sprbotfreedom.platform.base.service.impl.BaseServiceImpl;
import com.cybercloud.sprbotfreedom.platform.datasource.DB1DataSource;
import com.cybercloud.sprbotfreedom.platform.enums.SystemErrorCode;
import com.cybercloud.sprbotfreedom.platform.exception.ServiceException;
import com.cybercloud.sprbotfreedom.web.dao.db1.sys.user.SysRoleUserDAO;
import com.cybercloud.sprbotfreedom.web.entity.po.db1.sys.user.SysRoleUserEntity;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 系统角色用户关联业务层
 * @author liuyutang
 * @date 2024/2/29
 */
@Service
@PrintFunctionLog
@DB1DataSource
public class SysRoleUserServiceImpl extends BaseServiceImpl<SysRoleUserEntity, SysRoleUserDAO> implements SysRoleUserService {
    @Override
    public boolean bindUserAndRole(Long userId, Set<Long> roles) {
        if(userId == null || CollectionUtils.isEmpty(roles)){
            ServiceException.throwError(SystemErrorCode.ERROR_40002);
        }
        List<SysRoleUserEntity> sysRoleUserEntities = dao.selectList(
            normalStateLambdaQueryWrapper().lambda()
                .eq(SysRoleUserEntity::getUserId, userId)
        );
        if(CollectionUtils.isNotEmpty(sysRoleUserEntities)){
            defaultLogicDelete(sysRoleUserEntities);
        }
        List<SysRoleUserEntity> saveObjs  = new ArrayList<>();
        roles.forEach(roleId -> {
            saveObjs.add(
                    SysRoleUserEntity.builder().userId(userId).roleId(roleId).build()
            );
        });
        return CollectionUtils.isNotEmpty(defaultSave(saveObjs,true));
    }

    @Override
    public List<SysRoleUserEntity> findByUserId(Long userId) {
        if(userId == null){
            ServiceException.throwError(SystemErrorCode.ERROR_40002);
        }
        List<SysRoleUserEntity> sysRoleUserEntities = dao.selectList(
            normalStateLambdaQueryWrapper().lambda()
                .eq(SysRoleUserEntity::getUserId, userId)
        );
        if(CollectionUtils.isNotEmpty(sysRoleUserEntities)){
            return sysRoleUserEntities;
        }
        return null;
    }
}
