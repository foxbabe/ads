package com.sztouyun.advertisingsystem.viewmodel.account;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class UserInfoViewModel extends UpdateUserViewModel {

    @ApiModelProperty(value = "创建者名称")
    private String creator="";

    @ApiModelProperty(value = "用户头像")
    private String headPortrait;

    @ApiModelProperty(value = "维护客户数量")
    private Long customerCount;

    @ApiModelProperty(value = "合同签约数量")
    private Long signedContractCount;

    @ApiModelProperty(value = "合作方数量")
    private Long cooperationPartnerCount;


    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getHeadPortrait() {
        return headPortrait;
    }

    public void setHeadPortrait(String headPortrait) {
        this.headPortrait = headPortrait;
    }

    public Long getCustomerCount() {
        return customerCount  == null? 0 : customerCount;
    }

    public void setCustomerCount(Long customerCount) {
        this.customerCount = customerCount;
    }

    public Long getSignedContractCount() {
        return signedContractCount == null? 0 : signedContractCount;
    }

    public void setSignedContractCount(Long signedContractCount) {
        this.signedContractCount = signedContractCount;
    }

    public Long getCooperationPartnerCount() {
        return cooperationPartnerCount;
    }

    public void setCooperationPartnerCount(Long cooperationPartnerCount) {
        this.cooperationPartnerCount = cooperationPartnerCount;
    }
}
