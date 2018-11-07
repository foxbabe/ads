package com.sztouyun.advertisingsystem.viewmodel.monitor;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class PartnerAdvertisementStoreRelatedInfo {

    @ApiModelProperty(value = "展示次数")
    private Long totalDisplayTimes = 0L;

    @ApiModelProperty(value = "有效次数")
    private Long totalValidDisplayTimes = 0L;

    @ApiModelProperty(value = "投放门店数量")
    private Long storeCount;

    @ApiModelProperty(value = "请求次数")
    private Long requestTimes;
}
