package com.sztouyun.advertisingsystem.viewmodel.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * Created by szty on 2017/7/26.
 */
@ApiModel
public class MenuListItemViewModel extends CreateMenuViewModel {

    @ApiModelProperty(value = "菜单id")
    private String id;

    @ApiModelProperty(value = "从属目录")
    private String parentMenu;

    @ApiModelProperty(value = "是否根目录")
    private Boolean rootMenu;

    @ApiModelProperty(value = "更新时间")
    private Date updatedTime;

    @ApiModelProperty(value = "创建人")
    private String creatorName;

    @ApiModelProperty(value = "创建时间")
    private Date createdTime;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentMenu() {
        return parentMenu;
    }

    public void setParentMenu(String parentMenu) {
        this.parentMenu = parentMenu;
    }

    public Boolean getRootMenu() {
        return rootMenu;
    }

    public void setRootMenu(Boolean rootMenu) {
        this.rootMenu = rootMenu;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
