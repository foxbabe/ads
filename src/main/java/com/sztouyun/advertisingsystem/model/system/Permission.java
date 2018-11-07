package com.sztouyun.advertisingsystem.model.system;

import com.sztouyun.advertisingsystem.model.BaseModel;
import com.sztouyun.advertisingsystem.model.account.Role;

import javax.persistence.*;
import java.util.List;

/**
 * Created by szty on 2017/7/27.
 */
@Entity
public class Permission extends BaseModel {

    @Column(nullable = false,length=128)
    private String permissionName;

    @Column(nullable = false,length=128)
    private String apiUrl;

    @Column(name = "menu_id",nullable = false,length = 36)
    private String menuId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id",insertable = false,updatable = false)
    private Menu menu;

    @Column(name = "sort_number",columnDefinition = "Double default 0.0")
    private Double sortNumber;

    @ManyToMany(cascade=CascadeType.REFRESH,mappedBy="permissions")
    private List<Role> roles;

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

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

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public Double getSortNumber() {
        return sortNumber;
    }

    public void setSortNumber(Double sortNumber) {
        this.sortNumber = sortNumber;
    }
}
