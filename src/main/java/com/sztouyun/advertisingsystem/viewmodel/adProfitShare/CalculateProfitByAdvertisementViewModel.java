package com.sztouyun.advertisingsystem.viewmodel.adProfitShare;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@ApiModel
@Data
public class CalculateProfitByAdvertisementViewModel {
    @ApiModelProperty(value = "门店日收益的开始时间", required = true)
    @NotNull(message = "开始时间不能为空")
    @JsonFormat(pattern = Constant.DATA_YMD, timezone = "GMT+8")
    private Date startDate;

    @ApiModelProperty(value = "门店日收益的结束时间", required = true)
    @NotNull(message = "结束时间不能为空")
    @JsonFormat(pattern = Constant.DATA_YMD, timezone = "GMT+8")
    private Date endDate;

    @ApiModelProperty(value = "需要计算的广告ID")
    private List<String> calculateAdvertisementIds;

    @ApiModelProperty(value = "开机时长")
    private int bootHour;

    @ApiModelProperty(value = "开机时长")
    private int orderCount;

    @ApiModelProperty(value = "广告是否激活")
    private boolean active;
}
