package com.sztouyun.advertisingsystem.viewmodel.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;


/**
 * Created by szty on 2017/7/28.
 */
@ApiModel
public class CreatePermissionViewModel {

    @ApiModelProperty(value = "权限名称",required = true)
    @NotEmpty(message = "权限名称不能为空")
    private String permissionName;

    @ApiModelProperty(value = "权限路径",required = true)
    @NotEmpty(message = "权限路径不能为空")
    private String apiUrl;

    @ApiModelProperty(value = "菜单Id",required = true)
    @NotEmpty(message = "菜单Id不能为空")
    private String menuId;

    @ApiModelProperty(value = "权限排序",required = true)
    @NotNull(message = "权限排序不能为空")
    private Double sortNumber;

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public Double getSortNumber() {
        return sortNumber;
    }

    public void setSortNumber(Double sortNumber) {
        this.sortNumber = sortNumber;
    }
}
