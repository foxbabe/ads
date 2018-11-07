package com.sztouyun.advertisingsystem.viewmodel.contract;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

@ApiModel
public class ContractViewModel {
    @ApiModelProperty(value = "门店ID", required = true)
    @NotNull(message = "门店ID不能为空")
    private String storeId;

    @ApiModelProperty(value = "门店名称", required = true)
    @NotNull(message = "门店名称不能为空")
    private String storeName;

    @ApiModelProperty(value = "门店地址", required = true)
    @NotNull(message = "门店地址不能为空")
    private String storeAddress;

    @ApiModelProperty(value = "门店价位", required = true)
    private String storeCost;

    @ApiModelProperty(value = "备注", required = true)
    @NotNull(message = "备注不能为空")
    private String remark;

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public String getStoreCost() {
        return storeCost;
    }

    public void setStoreCost(String storeCost) {
        this.storeCost = storeCost;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


}
