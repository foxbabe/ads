package com.sztouyun.advertisingsystem.viewmodel.adProfitShare;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
@ApiModel
public class AdvertisementProfitStatisticViewModel {

    @ApiModelProperty(value = "广告分成总额")
    private String totalShareAmount;

    @ApiModelProperty(value = "已结算总额")
    private String settledAmount;

    @ApiModelProperty(value = "未结算总额")
    private String unsettledAmount;

    @ApiModelProperty(value = "广告排行列表")
    private List<AdvertisementProfitInfo> list = new ArrayList<>();

}
