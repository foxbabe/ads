package com.sztouyun.advertisingsystem.viewmodel.customer.CustomerVisit;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.*;
import java.util.Date;

@ApiModel
public class CreateCustomerVisitViewModel {

    @ApiModelProperty(value = "客户ID", required = true)
    @NotBlank(message = "客户ID不能为空")
    private String customerId;

    @ApiModelProperty(value = "拜访客户名称", required = true)
    @NotNull(message = "请输入拜访客户名称")
    @Length(max = 128,message = "客户名称超过长度")
    private String customerName;

    @ApiModelProperty(value = "客户拜访联系人", required = true)
    @NotBlank(message = "请输入拜访客户联系人")
    @Length(max = 50,message = "联系人超过长度")
    private String customerContacts;

    @ApiModelProperty(value = "客户拜访联系电话", required = true)
    @NotBlank(message = "客户联系电话不能为空")
    @Size(max = 20, message = "客户联系电话太长")
    @Pattern(regexp = Constant.REGEX_PHONE, message = "联系电话格式不准确！")
    private String customerContactNumber;

    @ApiModelProperty(value = "客户拜访联系邮箱", required = true)
    @Pattern(regexp = Constant.REGEX_EMAIL, message = "电子邮箱格式不准确！")
    @Size(max = 128, message = "邮件信息太长")
    private String customerEmail;

    @ApiModelProperty(value = "拜访时间",required = true)
    @JsonFormat(pattern = Constant.DATA_YMD,timezone = "GMT+8")
    @NotNull(message = "请选择客户拜访时间")
    @Past(message = "拜访时间必须小于等于当前时间")
    private Date visitTime;

    @ApiModelProperty(value = "拜访地点",required = true)
    @Length(max = 128,message = "拜访地址超过长度")
    private String address;

    @ApiModelProperty(value = "是否有合作意向",required = true)
    private Boolean hasIntention=true;

    @ApiModelProperty(value = "是否重点关注客户",required = true)
    private Boolean isVital=true;

    @ApiModelProperty(value = "拜访内容",required = true)
    @NotBlank(message = "请输入拜访客户内容")
    @Length(max = 2048,message = "拜访内容超过长度")
    private String remark;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Date getVisitTime() {
        return visitTime;
    }

    public void setVisitTime(Date visitTime) {
        this.visitTime = visitTime;
    }

    public Boolean getHasIntention() {
        return hasIntention;
    }

    public void setHasIntention(Boolean hasIntention) {
        this.hasIntention = hasIntention;
    }

    public Boolean getIsVital() {
        return isVital;
    }

    public void setIsVital(Boolean isVital) {
        this.isVital = isVital;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerContacts() {
        return customerContacts;
    }

    public void setCustomerContacts(String customerContacts) {
        this.customerContacts = customerContacts;
    }

    public String getCustomerContactNumber() {
        return customerContactNumber;
    }

    public void setCustomerContactNumber(String customerContactNumber) {
        this.customerContactNumber = customerContactNumber;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }
}
