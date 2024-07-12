package com.cybercloud.sprbotfreedom.web.entity.po.db1.sys.role;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cybercloud.sprbotfreedom.platform.base.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;


/**
 * 角色数据持久层实体
 * @author liuyutang
 * @date 2023/7/11
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@TableName("role")
public class SysRoleEntity extends BaseEntity implements Serializable {
    /**
     * 角色名称
     */
    private String name;
    /**
     * 角色代码
     */
    private String code;
    /**
     * 角色描述
     */
    private String remark;

    /**
     * 是否启用
     */
    private Boolean enabled;
    /**
     * 是否为管理员
     */
    private Boolean admin;

}
