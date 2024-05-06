package com.cybercloud.sprbotfreedom.web.service.db1.sys.role;

import com.cybercloud.sprbotfreedom.platform.base.service.BaseService;
import com.cybercloud.sprbotfreedom.web.entity.bo.resource.RoleResourceBO;
import com.cybercloud.sprbotfreedom.web.entity.po.db1.sys.resouce.SysResourceEntity;
import com.cybercloud.sprbotfreedom.web.entity.po.db1.sys.role.SysRoleResourceEntity;

import java.util.List;

/**
 * 角色资源关联接口
 * @author liuyutang
 * @date 2023/7/12
 */
public interface SysRoleResourceService extends BaseService<SysRoleResourceEntity> {

    /**
     * 查询角色拥有的资源列表
     * @param roleId
     * @param type
     * @return
     */
    List<SysResourceEntity> findResByRoleId(Long roleId, String type, Boolean enable);

    /**
     * 根据角色id获取资源列表
     * @param roleIds
     * @return
     */
    List<SysRoleResourceEntity> findRoleResByRoleId(Long ... roleIds);

    /**
     * 保存角色资源
     * @param save
     * @return
     */
    List<SysRoleResourceEntity> add(RoleResourceBO save);

    /**
     * 根绝角色id删除关联权限
     * @param roleId
     * @return
     */
    boolean del(Long roleId);

}
