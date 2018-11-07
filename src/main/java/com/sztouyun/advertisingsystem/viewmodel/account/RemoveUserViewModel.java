package com.sztouyun.advertisingsystem.viewmodel.account;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@ApiModel
public class RemoveUserViewModel {
    @ApiModelProperty(value = "组织ID", required = true)
    @NotBlank(message = "组织ID不能为空")
    private String organizationId;

    @ApiModelProperty(value = "人员ID", required = true)
    @NotBlank(message = "人员ID不能为空")
    private String userId;

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
