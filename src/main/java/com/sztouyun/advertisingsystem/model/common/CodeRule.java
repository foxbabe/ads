package com.sztouyun.advertisingsystem.model.common;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class CodeRule {

    @Id
    @Column(length = 50)
    private String codeType;

    @Column(nullable = false, length = 11)
    private int codeNumber;

    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }

    public int getCodeNumber() {
        return codeNumber;
    }

    public void setCodeNumber(int codeNumber) {
        this.codeNumber = codeNumber;
    }
}
