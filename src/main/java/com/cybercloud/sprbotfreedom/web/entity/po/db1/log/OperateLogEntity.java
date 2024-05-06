package com.cybercloud.sprbotfreedom.web.entity.po.db1.log;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

/**
 * 操作记录表
 * @author liuyutang
 * @date 2023/8/30
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document("operate_log")
public class OperateLogEntity {

    @Id
    @JsonIgnore
    private ObjectId objectId;
    @TableField("id")
    private Long id;
    /**
     * 操作时间
     */
    @Field("operateTime")
    private Date operateTime;
    /**
     * 操作信息
     */
    @Field("logInfo")
    private String logInfo;
    /**
     * 操作员名称
     */
    @Field("operatorName")
    private String operatorName;
    /**
     * 客户端IP
     */
    @Field("loginIp")
    private String loginIp;
}
