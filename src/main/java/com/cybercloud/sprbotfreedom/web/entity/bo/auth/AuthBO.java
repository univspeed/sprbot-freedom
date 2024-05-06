package com.cybercloud.sprbotfreedom.web.entity.bo.auth;

import com.cybercloud.sprbotfreedom.web.entity.po.db1.sys.resouce.SysResourceEntity;
import com.cybercloud.sprbotfreedom.web.entity.po.db1.sys.role.SysRoleEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 认证成功后响应业务实体
 * @author liuyutang
 * @date 2023/7/12
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthBO {
    /**
     * 认证token
     */
    private String token;
    /**
     * 用户名
     */
    private String username;
    /**
     * 用户id
     */
    private Long id;
    /**
     * 角色id
     */
    private List<SysRoleEntity> roles;
    /**
     * 权限
     */
    private List<SysResourceEntity> permission;
}
