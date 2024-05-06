package com.cybercloud.sprbotfreedom.web.entity.po.db1.sys.role;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cybercloud.sprbotfreedom.platform.base.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 角色资源关联数据实体
 * @author liuyutang
 * @date 2023/7/12
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_sys_role_resource")
public class SysRoleResourceEntity extends BaseEntity {
    /**
     * 资源id
     */
    private Long resourceId;

    /**
     * 资源编码
     */
    private String resourceCode;

    /**
     * 角色id
     */
    private Long roleId;
}
