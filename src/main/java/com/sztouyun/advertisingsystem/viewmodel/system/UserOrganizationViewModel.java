package com.sztouyun.advertisingsystem.viewmodel.system;

import com.sztouyun.advertisingsystem.viewmodel.account.UserDetailViewModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by szty on 2017/8/2.
 */
public class UserOrganizationViewModel extends UserDetailViewModel {

    @ApiModelProperty(value = "组织机构名称", required = true)
    private String organizationName;

    @ApiModelProperty(value= "角色类型编号",required = true)
    private Integer roleTypeNo;

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public Integer getRoleTypeNo() {
        return roleTypeNo;
    }

    public void setRoleTypeNo(Integer roleTypeNo) {
        this.roleTypeNo = roleTypeNo;
    }
}
