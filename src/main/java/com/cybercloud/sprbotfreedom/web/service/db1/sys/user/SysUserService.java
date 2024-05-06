package com.cybercloud.sprbotfreedom.web.service.db1.sys.user;

import com.cybercloud.sprbotfreedom.platform.base.entity.PageResult;
import com.cybercloud.sprbotfreedom.platform.base.service.BaseService;
import com.cybercloud.sprbotfreedom.web.entity.bo.user.ModifyPassBO;
import com.cybercloud.sprbotfreedom.web.entity.bo.user.SaveUserBO;
import com.cybercloud.sprbotfreedom.web.entity.po.db1.sys.user.SysUserEntity;

import java.util.List;

/**
 * 操作员业务层接口
 * @author liuyutang
 * @date 2023/7/11
 */
public interface SysUserService extends BaseService<SysUserEntity> {
    /**
     * 分页列表
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    PageResult<SysUserEntity> page(int page ,int pageSize,String name);

    /**
     * 用户列表
     * @param name
     * @return
     */
    List<SysUserEntity> findAll(String name);

    /**
     * 详情
     * @param id
     * @return
     */
    SysUserEntity findById(Long id);

    /**
     * 创建用户
     * @param user
     * @return
     */
    SysUserEntity add(SaveUserBO user);

    /**
     * 修改用户
     * @param old
     * @return
     */
    SysUserEntity update(SaveUserBO old);

    /**
     * 删除用户
     * @param id
     * @return
     */
    boolean del(Long id);

    /**
     * 修改密码
     * @param modifyPassBO
     * @return
     */
    boolean modifyPassword(ModifyPassBO modifyPassBO);

    /**
     * 更新最后一次登录时间
     * @param userId
     * @param time
     * @return
     */
    boolean modifyLastLoginTime(Long userId,String time);

    /**
     * 根据用户名查找
     * @param username
     * @return
     */
    SysUserEntity findByUserName(String username);
}
