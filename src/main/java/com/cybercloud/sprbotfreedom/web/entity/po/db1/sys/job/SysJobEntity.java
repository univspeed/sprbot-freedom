package com.cybercloud.sprbotfreedom.web.entity.po.db1.sys.job;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cybercloud.sprbotfreedom.platform.base.entity.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 定时任务数据实体
 * @author liuyutang
 * @date 2023/8/2
 */
@Data
@TableName("t_sys_job")
public class SysJobEntity extends BaseEntity implements Serializable {
    /** 类名称 */
    private String beanName;
    /** 方法名称 */
    private String methodName;
    /** 方法参数 */
    private String methodParams;
    /** cron表达式 */
    private String cronExpression;
    /** 作业状态 0 禁用，1 开启 */
    private Integer jobStatus;
    /** 备注 */
    private String remark;
}
