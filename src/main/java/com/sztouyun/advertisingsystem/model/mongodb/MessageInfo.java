package com.sztouyun.advertisingsystem.model.mongodb;

public class MessageInfo {
    public MessageInfo() {
    }

    public MessageInfo(String messageId, String content) {
        this.messageId = messageId;
        this.content = content;
    }

    private String messageId;
    private String content;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
