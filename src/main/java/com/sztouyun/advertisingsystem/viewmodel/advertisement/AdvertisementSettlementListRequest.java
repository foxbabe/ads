package com.sztouyun.advertisingsystem.viewmodel.advertisement;

import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class AdvertisementSettlementListRequest extends BasePageInfo {

    @ApiModelProperty(value = "广告ID",required = true)
    private String id;

    @ApiModelProperty(value = "是否结算")
    private Boolean settled;

}
