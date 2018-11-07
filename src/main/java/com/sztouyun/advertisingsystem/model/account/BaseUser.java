package com.sztouyun.advertisingsystem.model.account;

import com.sztouyun.advertisingsystem.model.BaseModel;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;


@MappedSuperclass
public abstract class BaseUser extends BaseModel  {
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(nullable = false,length=128)
    private String username;

    @Column(nullable = false,length=128)
    private String nickname="";

    @Column(nullable = false,length=128)
    private String password;

    @Column(nullable = false)
    private boolean enableFlag=true;

    @Column(nullable = false,columnDefinition = "tinyint(1) default 0")
    private boolean isAdmin;

    @Column(name = "organization_id",length = 36)
    protected String organizationId;

    @Column(name = "customer_id",length = 36)
    private String customerId;

    @Column(length=128)
    private String headPortrait;

    @Column(name = "role_id",nullable = false,length = 36)
    private String roleId;

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        if(StringUtils.isEmpty(organizationId)){
            this.organizationId=null;
        }else{
            this.organizationId = organizationId;
        }

    }

    public boolean isEnable() {
        return enableFlag;
    }

    public void setEnableFlag(boolean enableFlag) {
        this.enableFlag = enableFlag;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeadPortrait() {
        return headPortrait;
    }

    public void setHeadPortrait(String headPortrait) {
        this.headPortrait = headPortrait;
    }

    public boolean isEnableFlag() {
        return enableFlag;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}
