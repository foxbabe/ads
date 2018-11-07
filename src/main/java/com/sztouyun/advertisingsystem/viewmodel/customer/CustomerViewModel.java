package com.sztouyun.advertisingsystem.viewmodel.customer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * Created by szty on 2017/7/27.
 */
@ApiModel
public class CustomerViewModel {
    @ApiModelProperty(value = "客户ID", required = true)
    @NotNull(message = "客户ID不能为空")
    private String id;

    @ApiModelProperty(value = "客户编号", required = true)
    @NotNull(message = "客户编号不能为空")
    private String customerNumber;

    @ApiModelProperty(value = "客户头像", required = true)
    @NotNull(message = "客户头像不能为空")
    private String headPortrait;

    @ApiModelProperty(value = "客户名称", required = true)
    @NotNull(message = "客户名称不能为空")
    private String name;

    @ApiModelProperty(value = "从属行业", required = true)
    @NotNull(message = "从属行业不能为空")
    private String industry;

    @ApiModelProperty(value = "联系电话")
    private String contactNumber;

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getHeadPortrait() {
        return headPortrait;
    }

    public void setHeadPortrait(String headPortrait) {
        this.headPortrait = headPortrait;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }
}
