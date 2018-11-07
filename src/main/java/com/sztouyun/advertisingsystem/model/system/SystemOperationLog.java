package com.sztouyun.advertisingsystem.model.system;

import com.sztouyun.advertisingsystem.model.BaseModel;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class SystemOperationLog extends BaseModel {

    @Column(nullable = false)
    private Integer operation;

    public SystemOperationLog() {
    }

    public SystemOperationLog(Integer operation) {
        this.operation = operation;
    }

    public Integer getOperation() {
        return operation;
    }

    public void setOperation(Integer operation) {
        this.operation = operation;
    }

}
