package com.sztouyun.advertisingsystem.viewmodel.coorperationPartner;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class AdvertisementRequestCountViewModel {

    @ApiModelProperty("合作方名称")
    private String partnerName;

    @ApiModelProperty("合作方id")
    private String partnerId;

    @ApiModelProperty("请求广告数量")
    private long requestAdvertisementCount;

    @ApiModelProperty("请求广告占比")
    private String requestAdvertisementRatio;

    @ApiModelProperty("已展示广告数量")
    private long displayAdvertisementCount;

    @ApiModelProperty("已展示广告占比")
    private String displayAdvertisementRatio;

    @ApiModelProperty("未展示广告数量")
    private long noDisplayAdvertisementCount;

    @ApiModelProperty("未展示广告占比")
    private String noDisplayAdvertisementRatio;

}
