package com.cybercloud.sprbotfreedom.web.entity.bo.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户在线信息数据模型
 * @author liuyutang
 * @date 2023/7/28
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserOnlineBO implements Serializable {
    /** 用户ID */
    public String userId;
    /**  直播房间ID */
    public String roomId;
    /** 应用平台类型 */
    public String appSerTypeName;
    /** 分前端ID */
    public Integer edgeId;
    /** 分前端名称 */
    public String edgeName;
    /** 服务器IP地址 */
    public String serverIp;
    /** 服务器名称 */
    public String serverName;
    /** 桌面ID */
    private Integer desktopId;
    /** 应用平台类型 */
    public Integer appSerType;
    /** 终端类型 */
    public Integer terminalType;
    /** 终端类型 */
    public String tenantId;
    /** 推流目标 */
    public String outStreamTarget;
    /** 在线应用名称 */
    public String onlineAppName;
    /** 在线应用ID */
    public Integer onlineAppId;
    /** 分辨率 */
    public String resolution;
    /** 带宽 */
    public Integer bandWidth;
    /** 在线会话ID */
    public String onlineSessionId;
    /** 在线StartAPPTime */
    public String onlineAppStartTime;


    /**
     * 获取终端类型名称
     * @return
     */
    public String getTermtypeName(){
        int temp = this.terminalType << 4;
        temp = temp >> 28;
        switch (temp) {
            case 1:
                return "tv";
            case 2:
                return "pc";
            case 3:
                return "mobile";
            case 5:
                return "vr";
            case 6:
                return "web";
            default:
                return "mobile";
        }
    }

}
