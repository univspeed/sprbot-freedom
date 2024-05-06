package com.cybercloud.sprbotfreedom.web.entity.po.db1.sys.user;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cybercloud.sprbotfreedom.platform.base.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 操作员持久层对象
 * @author liuyutang
 * @date 2023/7/11
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_sys_user")
public class SysUserEntity extends BaseEntity {

    /**
     * 用户名，不能重复
     */
    @TableField(value = "username")
    private String username;
    /**
     * 操作员密码，MD5值
     */
    @JsonIgnore
    @TableField(value = "password")
    private String password;
    /**
     * 备注
     */
    @TableField(value = "Comment")
    private String comment;
    /**
     * 登录验证发送邮箱地址
     */
    @TableField(value = "email")
    private String email;
    /**
     * 登录验证码
     */
    @TableField(value = "verify_code")
    private String verifyCode;
    /**
     * 登录密码盐值
     */
    @JsonIgnore
    @TableField(value = "salt_value")
    private String saltValue;
    /**
     * 最后一次登录时间
     */
    @TableField(value = "last_login_time")
    private String lastLoginTime;
    /**
     * 最后一次变更密码时间
     */
    @TableField(value = "last_psw_time")
    private String lastPswTime;

}
