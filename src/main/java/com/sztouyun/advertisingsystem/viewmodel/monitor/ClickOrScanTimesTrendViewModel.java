package com.sztouyun.advertisingsystem.viewmodel.monitor;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@ApiModel
public class ClickOrScanTimesTrendViewModel {
    @JsonFormat(pattern = Constant.DATA_YMD)
    @ApiModelProperty("开始日期")
    private Date beginDate;

    @JsonFormat(pattern = Constant.DATA_YMD)
    @ApiModelProperty("结束日期")
    private Date endDate;

    @ApiModelProperty("点击次数")
    private Long clickTimes;

    @ApiModelProperty("扫码次数")
    private Long scanTimes;

    private List<ClickOrScanTimesTrendItem> item;
}
