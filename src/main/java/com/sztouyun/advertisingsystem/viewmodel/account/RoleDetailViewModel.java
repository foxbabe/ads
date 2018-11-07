package com.sztouyun.advertisingsystem.viewmodel.account;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.Date;

@ApiModel
public class RoleDetailViewModel {

    @ApiModelProperty(value = "角色ID", required = true)
    private String id;

    @ApiModelProperty(value = "角色名", required = true)
    private String roleName;

    @ApiModelProperty(value = "角色描述信息", required = true)
    private String description;

    @ApiModelProperty(value = "创建者名称", required = true)
    private String creator="";

    @ApiModelProperty(value = "角色类型名称", required = true)
    private String roleTypeName="";

    @ApiModelProperty(value = "角色类型", required = true)
    private Integer roleType;

    @ApiModelProperty(value = "用户是否启用", required = true)
    private boolean enableFlag;

    @ApiModelProperty(value = "创建时间", required = true)
    private Date createdTime;

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getRoleTypeName() {
        return roleTypeName;
    }

    public void setRoleTypeName(String roleTypeName) {
        this.roleTypeName = roleTypeName;
    }

    public Integer getRoleType() {
        return roleType;
    }

    public void setRoleType(Integer roleType) {
        this.roleType = roleType;
    }

    public boolean isEnable() {
        return enableFlag;
    }

    public void setEnableFlag(boolean enableFlag) {
        this.enableFlag = enableFlag;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoleName() {
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


}
