package com.sztouyun.advertisingsystem.viewmodel.partner;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class StoreRequestRankingViewModel {
    @ApiModelProperty("合作方名称")
    private String partnerName;

    @ApiModelProperty("门店资源数量")
    private Long storeNum;

    @ApiModelProperty("请求门店数量")
    private Long requestStoreNum;

    @ApiModelProperty("未请求门店数量")
    private Long noRequestStoreNum;
}
