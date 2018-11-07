package com.sztouyun.advertisingsystem.viewmodel.index;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by wenfeng on 2017/9/26.
 */
@ApiModel
public class AdPositionStatisticItem extends AdPositionStatisticDto{
    @ApiModelProperty(value = "区域名称", required = false)
    private String areaName;

    @ApiModelProperty(value = "区域可用用广告位数量", required = false)
    private Long availableAmount;

    @ApiModelProperty(value = "广告位占比", required = false)
    private String amountRatio;

    @ApiModelProperty(value = "广告位使用率", required = false)
    private String utilizationRatio;

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public Long getAvailableAmount() {
        return availableAmount;
    }

    public void setAvailableAmount(Long availableAmount) {
        this.availableAmount = availableAmount;
    }

    public String getAmountRatio() {
        return amountRatio;
    }

    public void setAmountRatio(String amountRatio) {
        this.amountRatio = amountRatio;
    }

    public String getUtilizationRatio() {
        return utilizationRatio;
    }

    public void setUtilizationRatio(String utilizationRatio) {
        this.utilizationRatio = utilizationRatio;
    }

    public void  setAvailableAmount(Long totalAmount,long usedAmount){
        this.availableAmount=totalAmount-usedAmount;
    }
}
