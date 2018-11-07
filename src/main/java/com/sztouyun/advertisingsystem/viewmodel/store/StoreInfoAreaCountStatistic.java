package com.sztouyun.advertisingsystem.viewmodel.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

@ApiModel
public class StoreInfoAreaCountStatistic extends StoreInfoUseCountStatistic{

    @ApiModelProperty(value = "地区分布数据")
    private List<StoreInfoAreaStatistic> storeInfoAreaStatistics = new ArrayList<>();

    public List<StoreInfoAreaStatistic> getStoreInfoAreaStatistics() {
        return storeInfoAreaStatistics;
    }

    public void setStoreInfoAreaStatistics(List<StoreInfoAreaStatistic> storeInfoAreaStatistics) {
        this.storeInfoAreaStatistics = storeInfoAreaStatistics;
    }

}
