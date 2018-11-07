package com.sztouyun.advertisingsystem.model.system;

import com.sztouyun.advertisingsystem.common.ITree;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by szty on 2017/7/26.
 */
@Entity
public class Area implements ITree{

    @Id
    @Column(name = "id",nullable = false,length = 20)
    private String id;
    @Column(name = "code",nullable = false,length = 20)
    private String code;
    @Column(name = "name",nullable = false,length = 50)
    private String name;
    @Column(name = "level",nullable = false,length = 4)
    private Integer level;
    @Column(name = "parent_id",nullable = false,length = 20)
    private String parentId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Area(){}
    public Area(String name, String id, String parentId) {
       this.setName(name);
       this.setId(id);
       this.setParentId(parentId);
    }
}
