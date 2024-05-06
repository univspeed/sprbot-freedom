package com.cybercloud.sprbotfreedom.web.entity.po.db1.file;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cybercloud.sprbotfreedom.platform.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * 附件持久层实体
 * @author liuyutang
 */
@Data
@ToString
@TableName("t_file_accessory")
@ApiModel(value="FileAccessory")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class FileAccessoryEntity extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 文件名称
     */
    @ApiModelProperty(value = "文件名称",name = "fileName",dataType="String")
    private String fileName;
    /**
     * 文件类型
     */
    @ApiModelProperty(value = "文件类型",name = "fileType",dataType="String")
    private String fileType;
    /**
     * 文件路径
     */
    @ApiModelProperty(value = "文件路径",name = "filePath",dataType="String")
    private String filePath;
    /**
     * 文件大小
     */
    @ApiModelProperty(value = "文件大小",name = "fileSize",dataType="Integer")
    private Long fileSize;
    /**
     * 关联表名
     */
    @ApiModelProperty(value = "关联表名",name = "relatedTable",dataType="String")
    private String relatedTable;
    /**
     * 关联字段名
     */
    @ApiModelProperty(value = "关联字段名",name = "relatedField",dataType="String")
    private String relatedField;
    /**
     * 关联业务
     */
    @ApiModelProperty(value = "关联业务",name = "relatedType",dataType="String")
    private String relatedType;
    /**
     * 关联ID
     */
    @ApiModelProperty(value = "关联ID",name = "relatedId",dataType="String")
    private Long relatedId;
    /**
     * 保留字段
     */
    @ApiModelProperty(value = "保留字段",name = "reserverd",dataType="String")
    private String reserverd;
    /**
     *
     */
    @ApiModelProperty(value = "",name = "syscode",dataType="String")
    private String syscode;


}
