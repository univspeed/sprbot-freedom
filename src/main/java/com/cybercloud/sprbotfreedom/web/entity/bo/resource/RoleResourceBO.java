package com.cybercloud.sprbotfreedom.web.entity.bo.resource;

import lombok.Data;

import java.util.Set;

/**
 * 角色资源业务数据载体
 * @author liuyutang
 * @date 2023/7/12
 */
@Data
public class RoleResourceBO {
    /**
     * 角色ID
     */
    private Long roleId;
    /**
     * 资源编码列表
     */
    private Set<String> resourceCode;
}
