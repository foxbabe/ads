package com.sztouyun.advertisingsystem.model.system;

import com.sztouyun.advertisingsystem.model.BaseModel;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Data
public class AuditReasonConfig extends BaseModel{
//    @Column(nullable = false)
    private Integer type;

    @Column(nullable = false)
    private String name;

    private String parentId;

    @Column(name = "count",columnDefinition = "Integer default 0")
    private int count;
}
