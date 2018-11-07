package com.sztouyun.advertisingsystem.model.common;

import com.sztouyun.advertisingsystem.model.BaseModel;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class VerificationCodeSendLog extends BaseModel{
    @Column(nullable = false,length=128)
    private String mobile;
    @Column(nullable = false,length=128)
    private String code;
    @Column(nullable = false)
    private int type;

    public VerificationCodeSendLog() {
    }

    public VerificationCodeSendLog(String mobile, String code, int type) {
        this.mobile = mobile;
        this.code = code;
        this.type = type;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
