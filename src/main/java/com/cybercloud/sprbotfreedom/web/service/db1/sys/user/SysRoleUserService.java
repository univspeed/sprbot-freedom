package com.cybercloud.sprbotfreedom.web.service.db1.sys.user;

import com.cybercloud.sprbotfreedom.platform.base.service.BaseService;
import com.cybercloud.sprbotfreedom.web.entity.po.db1.sys.user.SysRoleUserEntity;

import java.util.List;
import java.util.Set;

/**
 * 系统角色用户关联业务接口
 * @author liuyutang
 * @date 2024/2/29
 */
public interface SysRoleUserService extends BaseService<SysRoleUserEntity> {
    /**
     * 绑定用户、角色关系
     * @param userId 用户ID
     * @param roles 角色ID集合
     * @return 是否成功
     */
    boolean bindUserAndRole(Long userId, Set<Long> roles);

    /**
     * 根绝用户ID查询绑定关系集合
     * @param userId 用户id
     * @return 绑定关系
     */
    List<SysRoleUserEntity> findByUserId(Long userId);
}
