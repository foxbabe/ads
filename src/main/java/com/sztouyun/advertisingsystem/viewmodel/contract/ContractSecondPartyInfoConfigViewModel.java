package com.sztouyun.advertisingsystem.viewmodel.contract;

import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.annotation.validation.RegPattern;
import com.sztouyun.advertisingsystem.utils.StringUtils;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class ContractSecondPartyInfoConfigViewModel {


    @ApiModelProperty(value = "乙方名称", required = true)
    @Size(max = 128, message = "乙方名称太长")
    private String secondPartyName;

    @ApiModelProperty(value = "乙方责任联系人", required = true)
    @Size(max = 128, message = "乙方责任联系人太长")
    private String secondPartyResponsibilityPerson;

    @ApiModelProperty(value = "乙方联系电话", required = true)
    @Size(max = 128, message = "乙方联系电话太长")
    @RegPattern(regexp = Constant.REGEX_PHONE,nullable = true,message = "乙方联系电话格式错误")
    private String secondPartyPhone;

    @ApiModelProperty(value = "乙方指定送达地址", required = true)
    @Size(max = 2000, message = "乙方指定送达地址太长")
    private String secondPartyContractReceiveAddress;

    @ApiModelProperty(value = "乙方邮箱", required = true)
    @Size(max = 128, message = "乙方邮箱太长")
    @RegPattern(regexp = Constant.REGEX_EMAIL,nullable = true,message = "乙方邮箱格式错误")
    private String secondPartyEmail;

    public String getSecondPartyName() {
        return secondPartyName;
    }

    public void setSecondPartyName(String secondPartyName) {
        this.secondPartyName = StringUtils.getNotNullString(secondPartyName);
    }

    public String getSecondPartyResponsibilityPerson() {
        return secondPartyResponsibilityPerson;
    }

    public void setSecondPartyResponsibilityPerson(String secondPartyResponsibilityPerson) {
        this.secondPartyResponsibilityPerson = StringUtils.getNotNullString(secondPartyResponsibilityPerson);
    }

    public String getSecondPartyPhone() {
        return secondPartyPhone;
    }

    public void setSecondPartyPhone(String secondPartyPhone) {
        this.secondPartyPhone = StringUtils.getNotNullString(secondPartyPhone);
    }

    public String getSecondPartyContractReceiveAddress() {
        return secondPartyContractReceiveAddress;
    }

    public void setSecondPartyContractReceiveAddress(String secondPartyContractReceiveAddress) {
        this.secondPartyContractReceiveAddress = StringUtils.getNotNullString(secondPartyContractReceiveAddress);
    }

    public String getSecondPartyEmail() {
        return secondPartyEmail;
    }

    public void setSecondPartyEmail(String secondPartyEmail) {
        this.secondPartyEmail = StringUtils.getNotNullString(secondPartyEmail);
    }
}
