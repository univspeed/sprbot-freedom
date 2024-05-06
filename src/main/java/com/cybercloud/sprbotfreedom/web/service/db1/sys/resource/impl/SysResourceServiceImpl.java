package com.cybercloud.sprbotfreedom.web.service.db1.sys.resource.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cybercloud.sprbotfreedom.platform.annotation.log.PrintFunctionLog;
import com.cybercloud.sprbotfreedom.platform.base.entity.PageResult;
import com.cybercloud.sprbotfreedom.platform.base.service.impl.BaseServiceImpl;
import com.cybercloud.sprbotfreedom.platform.datasource.DB1DataSource;
import com.cybercloud.sprbotfreedom.platform.util.BeanUtil;
import com.cybercloud.sprbotfreedom.web.dao.db1.sys.resource.ResourceDAO;
import com.cybercloud.sprbotfreedom.web.entity.po.db1.sys.resouce.SysResourceEntity;
import com.cybercloud.sprbotfreedom.web.service.db1.sys.resource.SysResourceService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Set;

/**
 * 资源接口业务实现
 * @author liuyutang
 * @date 2023/7/12
 */
@Service
@PrintFunctionLog
@DB1DataSource
public class SysResourceServiceImpl extends BaseServiceImpl<SysResourceEntity, ResourceDAO> implements SysResourceService {

    @Override
    public PageResult<SysResourceEntity> page(int page, int pageSize, String name, String type, Long parent) {
        Page<SysResourceEntity> resourceEntityPage = dao.selectPage(getEmptyPage(page, pageSize),
                normalStateLambdaQueryWrapper().lambda()
                    .like(StringUtils.isNotBlank(name), SysResourceEntity::getResourceName, name)
                    .eq(StringUtils.isNotBlank(type), SysResourceEntity::getResourceType, type)
                    .eq(parent != null, SysResourceEntity::getParent, parent)
        );
        return PageResult.create(resourceEntityPage);
    }

    @Override
    public List<SysResourceEntity> findAll(String name, String type, Long parent) {

        return dao.selectList(
            normalStateLambdaQueryWrapper().lambda()
                .like(StringUtils.isNotBlank(name), SysResourceEntity::getResourceName, name)
                .eq(StringUtils.isNotBlank(type), SysResourceEntity::getResourceType, type)
                .eq(parent != null, SysResourceEntity::getParent, parent)
        );
    }

    @Override
    public SysResourceEntity findById(Long id) {

        return defaultFindOne(id);
    }

    @Override
    public SysResourceEntity findByCode(String resourceCode) {
        return dao.selectOne(normalStateLambdaQueryWrapper().lambda().eq(SysResourceEntity::getResourceCode,resourceCode));
    }

    @Override
    public List<SysResourceEntity> findByCode(Set<String> resourceCode) {
        if(CollectionUtils.isNotEmpty(resourceCode)){
            return dao.selectList(normalStateLambdaQueryWrapper().lambda().in(SysResourceEntity::getResourceCode,resourceCode));
        }
        return null;
    }

    @Override
    public SysResourceEntity add(SysResourceEntity resource) {
        String resourceCode = resource.getResourceCode();
        SysResourceEntity byCode = findByCode(resourceCode);
        Assert.isNull(byCode,"资源编码重复");
        return defaultSave(resource,true);
    }

    @Override
    public SysResourceEntity update(SysResourceEntity resource) {
        SysResourceEntity byId = findById(resource.getId());
        Assert.notNull(byId,"更新资源为空");
        String resourceCode = resource.getResourceCode();
        SysResourceEntity byCode = findByCode(resourceCode);
        if(byCode != null){
            if (!(resource.getId().longValue() == byCode.getId().longValue())) {
                Assert.isNull(byCode,"资源编码重复");
            }
        }
        SysResourceEntity sysResourceEntity = BeanUtil.copyProperties(resource, byId, true, true);
        return defaultUpdate(sysResourceEntity);
    }

    @Override
    public boolean disable(Long id) {
        SysResourceEntity byId = findById(id);
        Assert.notNull(byId,"更新资源为空");
        byId.setEnable(!byId.getEnable());
        return defaultUpdate(byId) != null;
    }

    @Override
    public boolean del(Long id) {

        return defaultLogicDelete(id) != null;
    }

    @Override
    public List<SysResourceEntity> findByCodes(Set<String> resourceCodes, Boolean enable, String type) {
        if(CollectionUtils.isNotEmpty(resourceCodes)){
            return dao.selectList(
                normalStateLambdaQueryWrapper().lambda()
                        .in(SysResourceEntity::getResourceCode,resourceCodes)
                        .eq(enable!=null, SysResourceEntity::getEnable,enable)
                        .eq(StringUtils.isNotBlank(type), SysResourceEntity::getResourceType,type)
            );
        }
        return getEmptyList();
    }
}
