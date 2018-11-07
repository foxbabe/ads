package com.sztouyun.advertisingsystem.viewmodel.message;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.model.common.RoleTypeEnum;
import com.sztouyun.advertisingsystem.service.account.AuthenticationService;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.experimental.var;

import java.util.Date;

@ApiModel
public class MessageViewModel {
    @ApiModelProperty(value = "消息Id")
    private String id;
    @ApiModelProperty(value = "消息类型")
    private int messageType;
    @ApiModelProperty(value = "消息子类")
    private int messageCategory;
    @ApiModelProperty(value = "操作人姓名")
    private String creatorName;
    @ApiModelProperty(value = "操作人Id")
    private String creatorId;
    @ApiModelProperty(value = "操作时间")
    @JsonFormat(pattern = Constant.TIME_HOUR, timezone = "GMT+8")
    private Date createdTime;
    private Boolean hasRead;

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Boolean getHasRead() {
        return hasRead;
    }

    public void setHasRead(Boolean hasRead) {
        this.hasRead = hasRead;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public int getMessageCategory() {
        return messageCategory;
    }

    public void setMessageCategory(int messageCategory) {
        this.messageCategory = messageCategory;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public boolean isManager() {
        var user  =AuthenticationService.getUser();
        if(user == null)
            return false;
        if(user.isAdmin())
            return true;
        if(user.getRoleTypeEnum().equals(RoleTypeEnum.ManagerialStaff))
            return true;
        return false;
    }

    public boolean isCreator() {
        return equalsCurrentUser(getCreatorId());
    }

    protected boolean equalsCurrentUser(String userId){
        var user  =AuthenticationService.getUser();
        if(user == null)
            return false;
        return user.getId().equals(userId);
    }

}
