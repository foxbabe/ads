package com.sztouyun.advertisingsystem.viewmodel.monitor;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class ContractMonitorDisplayTimesRatioInCashRegisterViewModel extends ContractMonitorDisplayTimesRatioViewModel {

    @ApiModelProperty(value = "投放城市数")
    private Integer totalCityCount;

    @ApiModelProperty(value = "投放门店数")
    private Integer totalStoreCount;

    public Integer getTotalCityCount() {
        return totalCityCount;
    }

    public void setTotalCityCount(Integer totalCityCount) {
        this.totalCityCount = totalCityCount;
    }

    public Integer getTotalStoreCount() {
        return totalStoreCount;
    }

    public void setTotalStoreCount(Integer totalStoreCount) {
        this.totalStoreCount = totalStoreCount;
    }
}
