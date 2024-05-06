package com.cybercloud.sprbotfreedom.platform.base.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.cybercloud.sprbotfreedom.web.entity.valid.SaveValid;
import com.cybercloud.sprbotfreedom.web.entity.valid.UpdateValid;
import com.github.jeffreyning.mybatisplus.anno.MppMultiId;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * 所有实体对象（VO,BO,DTO,PO）的基类
 * @author liuyutang
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString
public class BaseEntity implements Serializable {

    /**
     * 唯一id
     */
    @MppMultiId("ID")
    @NotNull(message = "id不能为空",groups = { UpdateValid.class})
    @ApiModelProperty(value = "",name = "id",dataType="String")
    protected Long id;
    /**
     * 创建时间
     */
    @Length(max = 20,message = "创建时间长度不能超过20位",groups = {SaveValid.class, UpdateValid.class})
    @ApiModelProperty(value = "创建时间",name = "createTime",dataType="String")
    @TableField("CREATE_TIME")
    protected String createTime;
    /**
     * 创建人
     */
    @Length(max = 32,message = "创建人长度不能超过32位",groups = {SaveValid.class, UpdateValid.class})
    @ApiModelProperty(value = "创建人",name = "createUser",dataType="String")
    @TableField("CREATE_USER")
    protected String createUser;
    /**
     * 更新时间
     */
    @Length(max = 20,message = "更新时间长度不能超过20位",groups = {SaveValid.class, UpdateValid.class})
    @ApiModelProperty(value = "更新时间",name = "updateTime",dataType="String")
    @TableField("UPDATE_TIME")
    protected String updateTime;
    /**
     * 更新人
     */
    @Length(max = 32,message = "更新人长度不能超过20位",groups = {SaveValid.class, UpdateValid.class})
    @ApiModelProperty(value = "更新人",name = "updateUser",dataType="String")
    @TableField("UPDATE_USER")
    protected String updateUser;
    /**
     * 数据状态 1 正常 0 删除
     */
    @ApiModelProperty(value = "数据状态 1正常 0删除",name = "state",dataType="String")
    @TableField("STATE")
    protected Integer state;
}
