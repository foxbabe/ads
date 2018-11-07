package com.sztouyun.advertisingsystem.model.message;

import com.sztouyun.advertisingsystem.model.account.User;
import com.sztouyun.advertisingsystem.utils.UUIDUtils;

import javax.persistence.*;

@Entity
public class MessageReceiver {

    public MessageReceiver() {
    }

    public MessageReceiver(String messageId, String receiverId) {
        this.messageId = messageId;
        this.receiverId = receiverId;
        this.hasRead = false;
    }

    @Id
    @Column(nullable = false,length = 36)
    private String id = UUIDUtils.generateOrderedUUID();

    @Column(name = "message_id",nullable = false, length = 36)
    private String messageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id",insertable = false,updatable = false)
    private Message message;

    @Column(name = "receiver_id",nullable = false,length = 36)
    private String receiverId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id",insertable = false, updatable = false)
    private User receiver;

    @Column(nullable = false)
    private boolean hasRead = false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public boolean isHasRead() {
        return hasRead;
    }

    public void setHasRead(boolean hasRead) {
        this.hasRead = hasRead;
    }
}
