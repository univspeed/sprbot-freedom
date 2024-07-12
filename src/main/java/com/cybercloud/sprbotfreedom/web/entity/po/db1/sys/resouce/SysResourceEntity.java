package com.cybercloud.sprbotfreedom.web.entity.po.db1.sys.resouce;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cybercloud.sprbotfreedom.platform.base.entity.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * @author liuyutang
 * @date 2023/7/12
 */
@Data
@TableName("resource")
public class SysResourceEntity extends BaseEntity implements Serializable {
    /**
     * 资源编码
     */
    private String resourceCode;
    /**
     * 资源名称
     */
    private String resourceName;
    /**
     * 资源类型（1、菜单 2、tab页 3、按钮 4、接口）
     */
    private String resourceType;
    /**
     * 路由地址
     */
    private String routeUrl;
    /**
     * 图标
     */
    private String icon;
    /**
     * 上级资源
     */
    private Long parent;
    /**
     * 是否启用
     */
    private Boolean enable;
    /**
     * 序号
     */
    private int idx;
}
