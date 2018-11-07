package com.sztouyun.advertisingsystem.viewmodel.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class StoreInfoUseCountByCityStatistic {

    @ApiModelProperty(value = "城市ID")
    private String areaId;

    @ApiModelProperty(value = "已经使用的门店数量")
    private Long storeCount;

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public Long getStoreCount() {
        return storeCount;
    }

    public void setStoreCount(Long storeCount) {
        this.storeCount = storeCount;
    }
}
