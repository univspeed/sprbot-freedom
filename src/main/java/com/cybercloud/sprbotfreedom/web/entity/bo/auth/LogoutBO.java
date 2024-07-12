package com.cybercloud.sprbotfreedom.web.entity.bo.auth;

import lombok.Data;

import java.io.Serializable;

/**
 * 系统退出登录业务数据载体
 * @author liuyutang
 * @date 2023/7/12
 */
@Data
public class LogoutBO  implements Serializable {
    private String username;

}
