package com.sztouyun.advertisingsystem.viewmodel.adProfitShare;

import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel
public class CalculateProfitByAdvertisementPageRequest extends BasePageInfo {

    @ApiModelProperty(value = "需要计算的广告ID")
    private List<String> calculateAdvertisementIds;

    public List<String> getCalculateAdvertisementIds() {
        return calculateAdvertisementIds;
    }

    public void setCalculateAdvertisementIds(List<String> calculateAdvertisementIds) {
        this.calculateAdvertisementIds = calculateAdvertisementIds;
    }
}
