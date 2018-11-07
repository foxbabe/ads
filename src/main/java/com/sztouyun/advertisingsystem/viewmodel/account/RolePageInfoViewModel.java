package com.sztouyun.advertisingsystem.viewmodel.account;

import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class RolePageInfoViewModel extends BasePageInfo {
    @ApiModelProperty(value = "模糊查询角色名")
    private String roleName;

    public String getRoleName() {
        if(this.roleName==null)
            return "";
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
