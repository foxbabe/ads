package com.sztouyun.advertisingsystem.viewmodel.index;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by wenfeng on 2017/9/26.
 */
@ApiModel
public class AdPositionStatisticDto {
    @ApiModelProperty(value = "区域ID", required = false)
    private String areaId;

    @ApiModelProperty(value = "区域广告位总数", required = false)
    private Long totalAmount;

    @ApiModelProperty(value = "区域广告位总数", required = false)
    private Long storeAmount;

    @ApiModelProperty(value = "区域已使用广告位数量", required = false)
    private Long usedAmount;

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public Long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Long getUsedAmount() {
        return usedAmount;
    }

    public void setUsedAmount(Long usedAmount) {
        this.usedAmount = usedAmount;
    }

    public Long getStoreAmount() {
        return storeAmount;
    }

    public void setStoreAmount(Long storeAmount) {
        this.storeAmount = storeAmount;
    }
}
