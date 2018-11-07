package com.sztouyun.advertisingsystem.viewmodel.advertisement;

import com.sztouyun.advertisingsystem.model.contract.ContractExtension;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.BeanUtils;

/**
 * Created by wenfeng on 2017/12/13.
 */
@ApiModel
public class CashRegisterAdvertisementConfigInfo extends TerminalAdvertisementConfigInfo {
    @ApiModelProperty(value = "投放门店总数")
    private Integer totalStores;

    @ApiModelProperty(value = "A类门店数量")
    private Integer storeACount;


    @ApiModelProperty(value = "B类门店数量")
    private Integer storeBCount;

    @ApiModelProperty(value = "C类门店数量")
    private Integer storeCCount;

    @ApiModelProperty(value = "D类门店数量")
    private Integer storeDCount;

    @ApiModelProperty(value = "广告总价")
    private double totalCost;
    public CashRegisterAdvertisementConfigInfo(){
        super();
    }
    public CashRegisterAdvertisementConfigInfo(Integer terminalType){
       super(terminalType);
    }


    public Integer getTotalStores() {
        return totalStores;
    }

    public void setTotalStores(Integer totalStores) {
        this.totalStores = totalStores;
    }

    public Integer getStoreACount() {
        return storeACount;
    }

    public void setStoreACount(Integer storeACount) {
        this.storeACount = storeACount;
    }

    public Integer getStoreBCount() {
        return storeBCount;
    }

    public void setStoreBCount(Integer storeBCount) {
        this.storeBCount = storeBCount;
    }

    public Integer getStoreCCount() {
        return storeCCount;
    }

    public void setStoreCCount(Integer storeCCount) {
        this.storeCCount = storeCCount;
    }

    public Integer getStoreDCount() {
        return storeDCount;
    }

    public void setStoreDCount(Integer storeDCount) {
        this.storeDCount = storeDCount;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    @Override
    public CashRegisterAdvertisementConfigInfo setSpecialConfig(ContractExtension contractExtension){
        if(contractExtension!=null){
            BeanUtils.copyProperties(contractExtension,this);
            this.setTotalCost(contractExtension.getTotalCost());
            this.setTotalStores(contractExtension.getTotalStoreCount());
        }
        return this;
    }
}
