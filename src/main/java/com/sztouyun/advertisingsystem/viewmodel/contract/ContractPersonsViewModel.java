package com.sztouyun.advertisingsystem.viewmodel.contract;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class ContractPersonsViewModel {

    @ApiModelProperty(value = "甲方名称")
    private String firstPartyName;

    @ApiModelProperty(value = "甲方责任联系人")
    private String firstPartyResponsibilityPerson;

    @ApiModelProperty(value = "甲方合同联系电话")
    private String firstPartyPhone;

    @ApiModelProperty(value = "甲方合同邮箱")
    private String firstPartyEmail;

    @ApiModelProperty(value = "甲方合同指定送达地址")
    private String firstPartyContractReceiveAddress;

    @ApiModelProperty(value = "乙方名称")
    private String secondPartyName;

    @ApiModelProperty(value = "乙方责任联系人")
    private String secondPartyResponsibilityPerson;

    @ApiModelProperty(value = "乙方联系电话")
    private String secondPartyPhone;

    @ApiModelProperty(value = "乙方指定送达地址")
    private String secondPartyContractReceiveAddress;

    @ApiModelProperty(value = "乙方邮箱")
    private String secondPartyEmail;

    public String getFirstPartyName() {
        return firstPartyName;
    }

    public void setFirstPartyName(String firstPartyName) {
        this.firstPartyName = firstPartyName;
    }

    public String getFirstPartyResponsibilityPerson() {
        return firstPartyResponsibilityPerson;
    }

    public void setFirstPartyResponsibilityPerson(String firstPartyResponsibilityPerson) {
        this.firstPartyResponsibilityPerson = firstPartyResponsibilityPerson;
    }

    public String getFirstPartyPhone() {
        return firstPartyPhone;
    }

    public void setFirstPartyPhone(String firstPartyPhone) {
        this.firstPartyPhone = firstPartyPhone;
    }

    public String getFirstPartyEmail() {
        return firstPartyEmail;
    }

    public void setFirstPartyEmail(String firstPartyEmail) {
        this.firstPartyEmail = firstPartyEmail;
    }

    public String getFirstPartyContractReceiveAddress() {
        return firstPartyContractReceiveAddress;
    }

    public void setFirstPartyContractReceiveAddress(String firstPartyContractReceiveAddress) {
        this.firstPartyContractReceiveAddress = firstPartyContractReceiveAddress;
    }

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
