package com.sztouyun.advertisingsystem.viewmodel.monitor;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * Created by wenfeng on 2018/3/8.
 */
@Data
@ApiModel
public class PartnerOrderDailyStoreMonitorStatisticItem extends BasePartnerOrderDailyStoreStatistic{
    @ApiModelProperty(value = "可用门店数")
    private Integer availableStoreCount=0;
    @ApiModelProperty(value = "不可用门店数")
    private Integer unavailableStoreCount=0;
    @ApiModelProperty(value = "激活门店数")
    private Integer activeStoreCount=0;
    @ApiModelProperty(value = "未激活门店数")
    private Integer inactiveStoreCount=0;

    public PartnerOrderDailyStoreMonitorStatisticItem() {
        super();
    }
    public PartnerOrderDailyStoreMonitorStatisticItem(Date date) {
        super(date);
    }
}
