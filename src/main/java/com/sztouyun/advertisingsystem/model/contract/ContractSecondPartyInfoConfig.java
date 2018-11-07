package com.sztouyun.advertisingsystem.model.contract;

import com.sztouyun.advertisingsystem.model.BaseModel;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class ContractSecondPartyInfoConfig extends BaseModel {

    /**
     *乙方名称
     */
    @Column(length=128, nullable = false)
    private String secondPartyName="";

    /**
     *责任联系人
     */
    @Column(length=128, nullable = false)
    private String secondPartyResponsibilityPerson="";

    /**
     *联系电话
     */
    @Column(length=128, nullable = false)
    private String secondPartyPhone="";

    /**
     *指定送达地址
     */
    @Column(length=2000, nullable = false)
    private String secondPartyContractReceiveAddress="";

    /**
     *邮箱
     */
    @Column(length=128, nullable = false)
    private String secondPartyEmail="";

    public String getSecondPartyName() {
        return secondPartyName;
    }

    public void setSecondPartyName(String secondPartyName) {
        this.secondPartyName = secondPartyName;
    }

    public String getSecondPartyResponsibilityPerson() {
        return secondPartyResponsibilityPerson;
    }

    public void setSecondPartyResponsibilityPerson(String secondPartyResponsibilityPerson) {
        this.secondPartyResponsibilityPerson = secondPartyResponsibilityPerson;
    }

    public String getSecondPartyPhone() {
        return secondPartyPhone;
    }

    public void setSecondPartyPhone(String secondPartyPhone) {
        this.secondPartyPhone = secondPartyPhone;
    }

    public String getSecondPartyContractReceiveAddress() {
        return secondPartyContractReceiveAddress;
    }

    public void setSecondPartyContractReceiveAddress(String secondPartyContractReceiveAddress) {
        this.secondPartyContractReceiveAddress = secondPartyContractReceiveAddress;
    }

    public String getSecondPartyEmail() {
        return secondPartyEmail;
    }

    public void setSecondPartyEmail(String secondPartyEmail) {
        this.secondPartyEmail = secondPartyEmail;
    }
}
