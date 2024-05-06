package com.cybercloud.sprbotfreedom.web.service.db1.sys.role;

import com.cybercloud.sprbotfreedom.platform.base.entity.PageResult;
import com.cybercloud.sprbotfreedom.platform.base.service.BaseService;
import com.cybercloud.sprbotfreedom.web.entity.po.db1.sys.role.SysRoleEntity;

import java.util.List;

/**
 * 角色服务
 * @author liuyutang
 * @date 2023/7/11
 */
public interface SysRoleService extends BaseService<SysRoleEntity> {

    /**
     * 获取角色分页列表
     * @param page
     * @param pageSize
     * @param roleName
     * @return
     */
    PageResult<SysRoleEntity> page(int page, int pageSize, String roleName);
    /**
     * 获取角色列表
     * @param roleName
     * @return
     */
    List<SysRoleEntity> findAll(String roleName);

    /**
     * 获取角色详情
     * @param id
     * @return
     */
    SysRoleEntity findById(Long id);

    /**
     * 新增角色
     * @param role
     * @return
     */
    SysRoleEntity add(SysRoleEntity role);

    /**
     * 更新角色
     * @param oldRole
     * @return
     */
    SysRoleEntity update(SysRoleEntity oldRole);

    /**
     * 删除角色
     * @param id
     * @return
     */
    boolean del(Long id);
}
