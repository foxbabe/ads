package com.sztouyun.advertisingsystem.viewmodel.adProfitShare;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel
@Data
public class PeriodDetailItemRequestViewModel extends BasePageInfo {
    @ApiModelProperty(value = "是否结算 true:已经结算  false:没有结算")
    private Boolean settled;

    @ApiModelProperty(value = "开始月份")
    @JsonFormat(pattern = Constant.DATA_YM, timezone = "GMT+8")
    private Date startDate;

    @ApiModelProperty(value = "结束月份")
    @JsonFormat(pattern = Constant.DATA_YM, timezone = "GMT+8")
    private Date endDate;

    @ApiModelProperty(value = "门店ID", required = true)
    private String storeId;
}
