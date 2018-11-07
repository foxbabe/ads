package com.sztouyun.advertisingsystem.viewmodel.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sztouyun.advertisingsystem.model.common.RoleTypeEnum;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@ApiModel
public class UserDetailViewModel extends UpdateUserViewModel {
    @ApiModelProperty(value = "用户是否启用", required = true)
    @NotNull(message = "启用标记不能为空")
    @JsonProperty("enable")
    private boolean enableFlag;

    @ApiModelProperty(value = "创建者名称", required = true)
    private String creator = "";

    @ApiModelProperty(value = "角色名称")
    private String roleName = "";

    @ApiModelProperty(value = "创建时间", required = true)
    private Date createdTime;

    @ApiModelProperty(value = "角色类型", required = true)
    private String roleType;

    @ApiModelProperty(value = "所属组织名称")
    private String organizationName;

    @ApiModelProperty(value = "上级领导Id")
    private String leaderId;

    @ApiModelProperty(value = "上级领导")
    private String leader;

    @ApiModelProperty(value = "角色类型编号")
    private Integer roleTypeNo;
}
