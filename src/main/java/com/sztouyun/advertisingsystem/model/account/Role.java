package com.sztouyun.advertisingsystem.model.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sztouyun.advertisingsystem.model.BaseModel;
import com.sztouyun.advertisingsystem.model.common.RoleTypeEnum;
import com.sztouyun.advertisingsystem.model.system.Menu;
import com.sztouyun.advertisingsystem.model.system.Permission;

import javax.persistence.*;
import java.util.List;


@Entity
public class Role extends BaseModel{
    @Column(nullable = false,length=128)
    private String roleName;

    @Column(nullable = false,length = 500)
    private String description="";

    @OneToMany(fetch = FetchType.LAZY,mappedBy="role")
    @JsonIgnore
    private List<User> users;

    @Column(nullable = false)
    private boolean enableFlag=true;

    @Column(nullable = false)
    private Integer roleType;

    @ManyToMany(cascade = CascadeType.PERSIST, fetch= FetchType.LAZY)
    @JoinTable(name="role_menu",joinColumns={@JoinColumn(name="role_id")},inverseJoinColumns={@JoinColumn(name="menu_id")})
    @JsonIgnore
    private List<Menu> menus;

    @ManyToMany(cascade = CascadeType.PERSIST, fetch= FetchType.EAGER)
    @JoinTable(name="role_permission",joinColumns={@JoinColumn(name="role_id")},inverseJoinColumns={@JoinColumn(name="permission_id")})
    @JsonIgnore
    private List<Permission> permissions;

    public Integer getRoleType() {
        return roleType;
    }

    public void setRoleType(Integer roleType) {
        this.roleType = roleType;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    public Role(){

    }

    public Role(String roleName, String description){
        this.roleName=roleName;
        this.description=description;
    }
    public Role(String roleName, String description,Integer roleType){
       this(roleName,description);
       this.roleType=roleType;
    }
    public String getRoleName() {
        if(getRoleType().equals(RoleTypeEnum.Admin.getValue()))
            return RoleTypeEnum.Admin.getDisplayName();
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Menu> getMenus() {
        return menus;
    }

    public void setMenus(List<Menu> menus) {
        this.menus = menus;
    }

    public boolean isEnable() {
        return enableFlag;
    }

    public void setEnableFlag(boolean enableFlag) {
        this.enableFlag = enableFlag;
    }

    public boolean isEnableFlag() {
        return enableFlag;
    }

}
