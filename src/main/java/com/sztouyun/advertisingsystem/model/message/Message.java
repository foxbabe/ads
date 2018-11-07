package com.sztouyun.advertisingsystem.model.message;

import com.sztouyun.advertisingsystem.model.BaseModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
public class Message extends BaseModel {

    @Transient
    private String content;

    @Column(nullable = false)
    private int messageType;

    @Column(nullable = false, length = 36)
    private String objectId;

    @Column(nullable = false)
    private int messageCategory;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

}
