package com.sztouyun.advertisingsystem.viewmodel.monitor;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@ApiModel
public class DisplayTimesBrokenChartRequest {
    @ApiModelProperty("广告ID")
    @NotNull(message = "广告ID不能为空")
    private String advertisementId;

    @ApiModelProperty(value = "开始日期(yyyy-MM-dd)", required = true)
    @JsonFormat(pattern = Constant.DATA_YMD, timezone = "GMT+8")
    @NotNull(message = "开始日期不能为空")
    private Date startDate;

    @ApiModelProperty(value = "结束日期(yyyy-MM-dd)", required = true)
    @JsonFormat(pattern = Constant.DATA_YMD, timezone = "GMT+8")
    @NotNull(message = "结束日期不能为空")
    private Date endDate;
}
