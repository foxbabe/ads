package com.sztouyun.advertisingsystem.viewmodel.adProfitShare;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel
public class StoreProfitCurveData {
    @ApiModelProperty(value = "日期(yyyy-MM-dd)")
    @JsonFormat(pattern = Constant.DATA_YMD, timezone = "GMT+8")
    private Date profitDate;

    @ApiModelProperty(value = "分成金额")
    private String shareAmount;

    @ApiModelProperty(value = "分成标准")
    private String shareStandard;

    @ApiModelProperty(value = "收益广告数量")
    private Integer effectiveAdvertisementCount;

    @ApiModelProperty(value = "收益广告占比")
    private String shareAdvertisementPr;
}
