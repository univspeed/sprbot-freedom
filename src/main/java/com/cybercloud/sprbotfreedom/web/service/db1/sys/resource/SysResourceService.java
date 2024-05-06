package com.cybercloud.sprbotfreedom.web.service.db1.sys.resource;

import com.cybercloud.sprbotfreedom.platform.base.entity.PageResult;
import com.cybercloud.sprbotfreedom.platform.base.service.BaseService;
import com.cybercloud.sprbotfreedom.web.entity.po.db1.sys.resouce.SysResourceEntity;

import java.util.List;
import java.util.Set;

/**
 * 资源接口
 * @author liuyutang
 * @date 2023/7/12
 */
public interface SysResourceService extends BaseService<SysResourceEntity> {
    /**
     * 资源分页列表
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    PageResult<SysResourceEntity> page(int page , int pageSize, String name, String type, Long parent);

    /**
     * 资源列表
     * @param name
     * @return
     */
    List<SysResourceEntity> findAll(String name, String type, Long parent);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    SysResourceEntity findById(Long id);

    /**
     * 根据资源编码查询
     * @param resourceCode
     * @return
     */
    SysResourceEntity findByCode(String resourceCode);

    /**
     * 根据资源编码查询
     * @param resourceCode
     * @return
     */
    List<SysResourceEntity> findByCode(Set<String> resourceCode);

    /**
     * 保存资源
     * @param resource
     * @return
     */
    SysResourceEntity add(SysResourceEntity resource);

    /**
     * 编辑资源
     * @param resource
     * @return
     */
    SysResourceEntity update(SysResourceEntity resource);

    /**
     * 禁用菜单
     * @param id
     * @return
     */
    boolean disable(Long id);

    /**
     * 删除菜单
     * @param id
     * @return
     */
    boolean del(Long id);

    /**
     * 根据代码查询有效的资源代码
     * @param resourceCodes
     * @param enable
     * @return
     */
    List<SysResourceEntity> findByCodes(Set<String> resourceCodes, Boolean enable, String type);
}
