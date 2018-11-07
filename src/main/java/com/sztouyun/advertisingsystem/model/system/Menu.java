package com.sztouyun.advertisingsystem.model.system;

import com.sztouyun.advertisingsystem.common.ITree;
import com.sztouyun.advertisingsystem.model.BaseModel;
import com.sztouyun.advertisingsystem.model.account.Role;

import javax.persistence.*;
import java.util.List;

/**
 * Created by szty on 2017/7/25.
 */
@Entity
public class Menu extends BaseModel implements ITree<String> {

    public static final String ROOT_PARENT_ID = "0";

    @Column(name="menu_name",nullable = false,length = 128)
    private String menuName;
    @Column(name="parent_id",length = 36)
    private String parentId;
    @Column(name="url", length = 255)
    private String url;
    @ManyToMany(cascade=CascadeType.REFRESH,mappedBy="menus")
    private List<Role> roles;

    @Column(name = "sort_number",columnDefinition = "Double default 0.0")
    private Double sortNumber;


    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public Double getSortNumber() {
        return sortNumber;
    }

    public void setSortNumber(Double sortNumber) {
        this.sortNumber = sortNumber;
    }
}
