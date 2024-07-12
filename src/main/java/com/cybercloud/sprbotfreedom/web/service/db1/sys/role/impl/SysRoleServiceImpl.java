package com.cybercloud.sprbotfreedom.web.service.db1.sys.role.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cybercloud.sprbotfreedom.platform.annotation.log.PrintFunctionLog;
import com.cybercloud.sprbotfreedom.platform.base.entity.PageResult;
import com.cybercloud.sprbotfreedom.platform.base.service.impl.BaseServiceImpl;
import com.cybercloud.sprbotfreedom.platform.datasource.annotation.DB1DataSource;
import com.cybercloud.sprbotfreedom.platform.util.BeanUtil;
import com.cybercloud.sprbotfreedom.web.dao.db1.sys.role.RoleDAO;
import com.cybercloud.sprbotfreedom.web.entity.po.db1.sys.role.SysRoleEntity;
import com.cybercloud.sprbotfreedom.web.service.db1.sys.role.SysRoleService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

/**
 * 角色服务接口功能实现
 * @author liuyutang
 * @date 2023/7/11
 */
@Service
@PrintFunctionLog
@DB1DataSource
public class SysRoleServiceImpl extends BaseServiceImpl<SysRoleEntity,RoleDAO> implements SysRoleService {

    @Override
    public PageResult<SysRoleEntity> page(int page, int pageSize, String roleName) {
        Page<SysRoleEntity> roleEntityPage = dao.selectPage(getEmptyPage(page, pageSize),
                normalStateLambdaQueryWrapper().lambda()
                        .like(StringUtils.isNotBlank(roleName), SysRoleEntity::getName, roleName)
        );
        return PageResult.create(roleEntityPage);
    }
    @Override
    public List<SysRoleEntity> findAll(String roleName) {
        return dao.selectList(
            normalStateLambdaQueryWrapper().lambda()
                .like(StringUtils.isNotBlank(roleName), SysRoleEntity::getName,roleName)
        );
    }

    @Override
    public SysRoleEntity findById(Long id) {

        return defaultFindOne(id);
    }

    @Override
    public SysRoleEntity add(SysRoleEntity role) {
        return role == null ? null : defaultSave(role,true);
    }

    @Override
    public SysRoleEntity update(SysRoleEntity oldRole) {
        SysRoleEntity sysRoleEntity = null;
        try {
            sysRoleEntity = defaultFindOne(oldRole.getId());
            Assert.notNull(sysRoleEntity, "修改角色对象不存在");
            sysRoleEntity = BeanUtil.copyProperties(oldRole, sysRoleEntity, true, true);
        }catch (IllegalArgumentException e){
            throw e;
        }
        return defaultUpdate(sysRoleEntity);
    }

    @Override
    public boolean del(Long id) {
        return CollectionUtils.isNotEmpty(defaultLogicDelete(id));
    }
}
