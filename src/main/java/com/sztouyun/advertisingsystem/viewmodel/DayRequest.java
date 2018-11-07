package com.sztouyun.advertisingsystem.viewmodel;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * Created by szty on 2018/8/9.
 */
@Data
public class DayRequest {
    @ApiModelProperty(value = "请求日期")
    @JsonFormat(pattern = Constant.DATA_YMD, timezone = "GMT+8")
    private Date date;
}
