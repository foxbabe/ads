package com.sztouyun.advertisingsystem.viewmodel.store;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel
@Data
public class StoreNetworkPeriodIntervalChartRequest {

    @ApiModelProperty(value = "日期")
    @JsonFormat(pattern = Constant.DATA_YMD, timezone = "GMT+8")
    private Date date;

    @ApiModelProperty(value = "时段区间")
    private Integer interval;
}
