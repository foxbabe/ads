package com.sztouyun.advertisingsystem.viewmodel.adProfitShare;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by wenfeng on 2018/1/24.
 */
@ApiModel
public class SettleProfitViewModel {
    @ApiModelProperty(value = "结算ID")
    private String id;
    @ApiModelProperty(value = "结算总金额")
    private String settleAmount;
    @ApiModelProperty(value = "结算门店数")
    private Integer settleStoreCount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSettleAmount() {
        return settleAmount;
    }

    public void setSettleAmount(String settleAmount) {
        this.settleAmount = settleAmount;
    }

    public Integer getSettleStoreCount() {
        return settleStoreCount;
    }

    public void setSettleStoreCount(Integer settleStoreCount) {
        this.settleStoreCount = settleStoreCount;
    }
    public SettleProfitViewModel(){
        this.id = "";
        this.settleAmount = "0";
        this.settleStoreCount = 0;
    }
    public SettleProfitViewModel(String id, String settleAmount, Integer settleStoreCount) {
        this.id = id;
        this.settleAmount = settleAmount;
        this.settleStoreCount = settleStoreCount;
    }
}
