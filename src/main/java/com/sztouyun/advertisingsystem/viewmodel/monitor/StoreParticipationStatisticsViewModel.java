package com.sztouyun.advertisingsystem.viewmodel.monitor;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class StoreParticipationStatisticsViewModel {
    @ApiModelProperty("广告位名称")
    private String AdvertisementPositionName;

    @ApiModelProperty("链接方式: Url点击, 二维码")
    private String linkType;

    @ApiModelProperty("参与门店")
    private Long participatingStoreNum;

    @ApiModelProperty("未参与门店")
    private Long noParticipatingStoreNum;
}
