package com.sztouyun.advertisingsystem.viewmodel.coorperationPartner;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartnerAdvertisementCountStatisticInfo {

    @ApiModelProperty(value = "全部第三方广告数量")
    private Long totalCount;

    @ApiModelProperty(value = "投放中")
    private Long deliveringCount;

    @ApiModelProperty(value = "已下架")
    private Long takeOffCount;

    @ApiModelProperty(value ="已完成")
    private Long finishedCount;
}
