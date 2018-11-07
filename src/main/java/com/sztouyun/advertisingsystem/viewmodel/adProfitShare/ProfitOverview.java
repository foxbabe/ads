package com.sztouyun.advertisingsystem.viewmodel.adProfitShare;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@ApiModel
public class ProfitOverview {
    @ApiModelProperty(value = "系统分成总额")
    private Double totalShareAmount;

    @ApiModelProperty(value = "时间周期：开始日期(yyyy年MM月dd日)")
    @JsonFormat(pattern = Constant.DATA_YMD_CN, timezone = "GMT+8")
    private Date startDate;

    @ApiModelProperty(value = "时间周期：结束日期(yyyy年MM月dd日)")
    @JsonFormat(pattern = Constant.DATA_YMD_CN, timezone = "GMT+8")
    private Date endDate;

    @ApiModelProperty(value = "已结算总额")
    private Double settledAmount;

    @ApiModelProperty(value = "未结算总额")
    private Double unsettledAmount;

    private List<ProfitOverviewDataViewModel> profitOverviewDataList;
}
