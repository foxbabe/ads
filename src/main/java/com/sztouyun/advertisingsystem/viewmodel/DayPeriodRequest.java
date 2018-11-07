package com.sztouyun.advertisingsystem.viewmodel;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * Created by szty on 2018/9/5.
 */
@ApiModel
@Data
public class DayPeriodRequest {
    @ApiModelProperty(value = "开始日期")
    @JsonFormat(pattern = Constant.DATA_YMD, timezone = "GMT+8")
    private Date startTime;
    @ApiModelProperty(value = "结束日期")
    @JsonFormat(pattern = Constant.DATA_YMD, timezone = "GMT+8")
    private Date endTime=new Date();
}
