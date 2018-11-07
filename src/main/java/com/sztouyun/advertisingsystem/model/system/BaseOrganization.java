package com.sztouyun.advertisingsystem.model.system;

import com.sztouyun.advertisingsystem.model.BaseModel;
import com.sztouyun.advertisingsystem.model.account.User;

import javax.persistence.*;

@MappedSuperclass
public abstract class BaseOrganization extends BaseModel {

    public static final String ROOT_PARENT_ID = "0";

    @Column(nullable = false, length = 256)
    private String code;

    @Column(nullable = true, length = 36, name = "parent_id")
    private String parentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id",insertable = false,updatable = false)
    private Organization parent;

    @Column(nullable = false, length = 128)
    private String name;

    @Column(name = "owner_id",length = 36)
    private String ownerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id",insertable = false,updatable = false)
    private User owner;

    public static String getRootParentId() {
        return ROOT_PARENT_ID;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Organization getParent() {
        return parent;
    }

    public void setParent(Organization parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }
}
