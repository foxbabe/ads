package com.sztouyun.advertisingsystem.viewmodel.account;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@ApiModel
public class UpdateRoleViewModel extends CreateRoleViewModel{
    @ApiModelProperty(value = "角色ID", required = true)
    @NotBlank(message = "角色ID不能为空")
    private String id;

    @ApiModelProperty(hidden = true)
    private Integer permissionCount;

    public Integer getPermissionCount() {
        return permissionCount;
    }

    public void setPermissionCount(Integer permissionCount) {
        this.permissionCount = permissionCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
