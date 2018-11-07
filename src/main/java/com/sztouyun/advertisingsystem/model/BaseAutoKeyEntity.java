package com.sztouyun.advertisingsystem.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Data
public abstract class BaseAutoKeyEntity {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @Column(nullable = false,updatable = false)
    @CreatedDate
    private Date createdTime;

    @Column(nullable = false)
    @LastModifiedDate
    private Date updatedTime;
}
