package com.sztouyun.advertisingsystem.viewmodel.coorperationPartner;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by szty on 2018/7/30.
 */
@Data
public class PartnerAdvertisementDisplayInfo {
    @ApiModelProperty(value = "合作方广告ID")
    private String partnerAdvertisementId;
    @ApiModelProperty(value = "三方广告ID")
    private String thirdPartId;
    @ApiModelProperty(value = "投放门店数")
    private Integer storeCount;
    @ApiModelProperty(value = "请求次数")
    private Long requestTimes;
    @ApiModelProperty(value = "展示次数")
    private Long displayTimes;
    @ApiModelProperty(value = "有效展示次数")
    private Long validTimes;
}
