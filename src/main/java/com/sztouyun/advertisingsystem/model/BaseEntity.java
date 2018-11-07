package com.sztouyun.advertisingsystem.model;

import com.sztouyun.advertisingsystem.utils.UUIDUtils;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {
    @Id
    @Column(nullable = false, length = 36)
    private String id = UUIDUtils.generateOrderedUUID();

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private Date createdTime;

    @Column(nullable = false)
    @LastModifiedDate
    private Date updatedTime;
}
