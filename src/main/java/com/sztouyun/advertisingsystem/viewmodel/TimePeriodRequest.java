package com.sztouyun.advertisingsystem.viewmodel;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Past;
import java.util.Date;

/**
 * Created by szty on 2018/7/31.
 */
@Data
public class TimePeriodRequest {
    @ApiModelProperty(value = "开始时间")
    @JsonFormat(pattern = Constant.DATETIME, timezone = "GMT+8")
    @Past(message = "开始时间必须在当前时间之前")
    private Date startDate;

    @ApiModelProperty(value = "结束时间")
    @JsonFormat(pattern = Constant.DATETIME, timezone = "GMT+8")
    @Past(message = "结束时间必须在当前时间之前")
    private Date endDate;
}
