package com.sztouyun.advertisingsystem.viewmodel.monitor;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel
public class ClickOrScanTimesTrendItem {
    @JsonFormat(pattern = Constant.DATA_YMD)
    @ApiModelProperty("日期")
    private Date date;

    @ApiModelProperty("点击次数")
    private Long clickTimes;

    @ApiModelProperty("扫码次数")
    private Long scanTimes;
}
