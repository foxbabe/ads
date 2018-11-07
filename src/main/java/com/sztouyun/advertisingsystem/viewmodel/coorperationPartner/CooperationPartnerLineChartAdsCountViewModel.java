package com.sztouyun.advertisingsystem.viewmodel.coorperationPartner;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@ApiModel
@Data
public class CooperationPartnerLineChartAdsCountViewModel {

    @ApiModelProperty(value = "每天的请求广告数量信息")
    private List<CooperationPartnerLineChartAdsCountInfo> cooperationPartnerLineChartAdsCountInfos = new ArrayList<>();

    @ApiModelProperty("请求广告数量")
    private long requestAdvertisementCount;

    @ApiModelProperty("已展示广告数量")
    private long displayAdvertisementCount;

    @ApiModelProperty("未展示广告数量")
    private long noDisplayAdvertisementCount;

    public long getNoDisplayAdvertisementCount() {
        return getRequestAdvertisementCount()-getDisplayAdvertisementCount();
    }
}
