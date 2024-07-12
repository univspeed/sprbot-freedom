package com.cybercloud.sprbotfreedom.web.service.db1.sys.role.impl;

import com.cybercloud.sprbotfreedom.platform.annotation.log.PrintFunctionLog;
import com.cybercloud.sprbotfreedom.platform.base.service.impl.BaseServiceImpl;
import com.cybercloud.sprbotfreedom.platform.datasource.annotation.DB1DataSource;
import com.cybercloud.sprbotfreedom.web.dao.db1.sys.role.RoleResourceDAO;
import com.cybercloud.sprbotfreedom.web.entity.bo.resource.RoleResourceBO;
import com.cybercloud.sprbotfreedom.web.entity.po.db1.sys.resouce.SysResourceEntity;
import com.cybercloud.sprbotfreedom.web.entity.po.db1.sys.role.SysRoleEntity;
import com.cybercloud.sprbotfreedom.web.entity.po.db1.sys.role.SysRoleResourceEntity;
import com.cybercloud.sprbotfreedom.web.service.db1.sys.resource.SysResourceService;
import com.cybercloud.sprbotfreedom.web.service.db1.sys.role.SysRoleResourceService;
import com.cybercloud.sprbotfreedom.web.service.db1.sys.role.SysRoleService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 角色资源关联业务接口实现
 * @author liuyutang
 * @date 2023/7/12
 */
@Service
@PrintFunctionLog
@DB1DataSource
public class SysRoleResourceServiceImpl extends BaseServiceImpl<SysRoleResourceEntity, RoleResourceDAO> implements SysRoleResourceService {

    private final SysResourceService sysResourceService;
    private final SysRoleService sysRoleService;

    public SysRoleResourceServiceImpl(SysResourceService sysResourceService, SysRoleService sysRoleService) {
        this.sysResourceService = sysResourceService;
        this.sysRoleService = sysRoleService;
    }

    @Override
    public List<SysResourceEntity> findResByRoleId(Long roleId, String type, Boolean enable) {
        SysRoleEntity byId = sysRoleService.findById(roleId);
        Assert.notNull(byId,"关联角色为空");
        List<SysRoleResourceEntity> roleResByRoleId = findRoleResByRoleId(roleId);
        if(CollectionUtils.isNotEmpty(roleResByRoleId)){
            Set<String> resourceCodes = roleResByRoleId.stream().map(SysRoleResourceEntity::getResourceCode).collect(Collectors.toSet());
            return sysResourceService.findByCodes(resourceCodes,enable,type);
        }
        return getEmptyList();
    }

    @Override
    public List<SysRoleResourceEntity> findRoleResByRoleId(Long ... roleIds) {
        Assert.isTrue(ArrayUtils.isNotEmpty(roleIds),"角色ID不能为空");
        Map<Long, SysRoleEntity> roles = sysRoleService.defaultFindByIds(roleIds);
        Assert.isTrue(MapUtils.isNotEmpty(roles),"关联角色为空");
        return dao.selectList(
                normalStateLambdaQueryWrapper().lambda()
                    .in(SysRoleResourceEntity::getRoleId, roleIds)
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<SysRoleResourceEntity> add(RoleResourceBO save) {
        Long roleId = save.getRoleId();
        Assert.notNull(roleId,"角色id不能为空");
        SysRoleEntity byId = sysRoleService.findById(roleId);
        Assert.notNull(byId,"关联角色为空");
        del(roleId);
        Set<String> resourceCodes = save.getResourceCode();
        List<SysResourceEntity> byCode = sysResourceService.findByCode(resourceCodes);
        if(CollectionUtils.isNotEmpty(byCode)){
            List<SysRoleResourceEntity> resourceEntities = new ArrayList<>();
            for (SysResourceEntity resourceCode : byCode) {
                resourceEntities.add(
                        SysRoleResourceEntity.builder()
                                .roleId(roleId)
                                .resourceId(resourceCode.getId())
                                .resourceCode(resourceCode.getResourceCode())
                                .build()
                );
            }
            return defaultSave(resourceEntities,true);
        }
        return getEmptyList();
    }

    @Override
    public boolean del(Long roleId) {
        Assert.notNull(roleId,"角色ID不能为空");
        int update = dao.update(
                updateInfo(SysRoleResourceEntity.builder().state(0).build()),
                normalStateLambdaQueryWrapper().lambda()
                        .eq(SysRoleResourceEntity::getRoleId, roleId)
        );
        return update>0;
    }
}
