package com.sztouyun.advertisingsystem.viewmodel.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class StoreInfoUseCountStatistic {

    @ApiModelProperty(value = "已经使用的门店数量")
    private Integer useStoreCount = 0;

    @ApiModelProperty(value = "未使用的门店数量")
    private Integer unUseStoreCount = 0;

    @ApiModelProperty(value = "总门店数量")
    private Integer allStoreCount = 0;

    public StoreInfoUseCountStatistic(Integer useStoreCount, Integer allStoreCount) {
        this.useStoreCount = useStoreCount;
        this.allStoreCount = allStoreCount;
    }

    public StoreInfoUseCountStatistic() {
    }

    public Integer getUseStoreCount() {
        return useStoreCount;
    }

    public void setUseStoreCount(Integer useStoreCount) {
        this.useStoreCount = useStoreCount;
    }

    public Integer getUnUseStoreCount() {
        return this.allStoreCount-this.useStoreCount;
    }

    public void setUnUseStoreCount(Integer unUseStoreCount) {
        this.unUseStoreCount = unUseStoreCount;
    }

    public Integer getAllStoreCount() {
        return allStoreCount;
    }

    public void setAllStoreCount(Integer allStoreCount) {
        this.allStoreCount = allStoreCount;
    }
}
