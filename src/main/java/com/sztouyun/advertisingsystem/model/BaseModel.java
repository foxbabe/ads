package com.sztouyun.advertisingsystem.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sztouyun.advertisingsystem.model.account.User;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseModel extends BaseEntity implements Serializable {
    @Column(name = "creator_id",updatable = false,length = 36)
    @CreatedBy
    private String creatorId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id",insertable = false,updatable = false)
    @JsonIgnore
    private User creator;

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public User getCreator() {
        return creator;
    }
}
