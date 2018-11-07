package com.sztouyun.advertisingsystem.viewmodel.adProfitShare;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel
@Data
public class PeriodDetailItemViewModel {

    @ApiModelProperty(value = "每月日期")
    @JsonFormat(pattern = Constant.DATA_YM_CN, timezone = "GMT+8")
    private Date date;

    @ApiModelProperty(value = "分成金额")
    private Double shareAmount;

    @ApiModelProperty(value = "是否结算 true:已经结算  false:没有结算")
    private Boolean settled;

    @ApiModelProperty(value = "每月开始日期")
    @JsonFormat(pattern = Constant.DATA_YMD, timezone = "GMT+8")
    private Date startDate;

    @ApiModelProperty(value = "每月结束日期")
    @JsonFormat(pattern = Constant.DATA_YMD, timezone = "GMT+8")
    private Date endDate;

}
