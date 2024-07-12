package com.cybercloud.sprbotfreedom.web.entity.po.db1.sys.user;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cybercloud.sprbotfreedom.platform.base.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * 操作员持久层对象
 * @author liuyutang
 * @date 2023/7/11
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_sys_role_user")
public class SysRoleUserEntity extends BaseEntity implements Serializable {
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 角色ID
     */
    private Long roleId;
}
