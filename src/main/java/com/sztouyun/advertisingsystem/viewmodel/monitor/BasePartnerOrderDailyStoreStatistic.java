package com.sztouyun.advertisingsystem.viewmodel.monitor;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * Created by wenfeng on 2018/3/8.
 */
@ApiModel
@Data
public class BasePartnerOrderDailyStoreStatistic {
    @ApiModelProperty(value = "时间")
    @JsonFormat(pattern = Constant.DATA_YMD_CN, timezone = "GMT+8")
    private Date date;
    @ApiModelProperty(value = "投放门店总数")
    private Integer totalStoreCount=0;

    public BasePartnerOrderDailyStoreStatistic() {
    }
    public BasePartnerOrderDailyStoreStatistic(Date date) {
        this.date = date;
    }
}
