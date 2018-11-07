package com.sztouyun.advertisingsystem.viewmodel.monitor;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by wenfeng on 2018/3/9.
 */
@ApiModel
@Data
public class PartnerOrderDetailDailyDeliveryStatisticViewModel {
    @ApiModelProperty("已展示总数")
    private  Integer totalDisplayTimes=0;

    @ApiModelProperty("每月投放状况列表")
    private List<PartnerOrderDetailDailyDeliveryStatisticItem> list;

}
