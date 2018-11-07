package com.sztouyun.advertisingsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sztouyun.advertisingsystem.model.account.User;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseRecordModel extends BaseModel {
    @Column(name = "updater_id",length = 36)
    @LastModifiedBy
    private String updaterId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updater_id",insertable = false,updatable = false)
    @JsonIgnore
    private User updater;


    public String getUpdaterId() {
        return updaterId;
    }

    public User getUpdater() {
        return updater;
    }

    public void setUpdaterId(String updaterId) {
        this.updaterId = updaterId;
    }

    public void setUpdater(User updater) {
        this.updater = updater;
    }
}
