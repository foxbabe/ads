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
public class DisplayTimesBrokenChartViewModel {
    @ApiModelProperty("时间周期：开始日期(yyyy年MM月dd日)")
    @JsonFormat(pattern = Constant.DATA_YMD_CN, timezone = "GMT+8")
    private Date startDate;

    @ApiModelProperty("时间周期：结束日期(yyyy年MM月dd日)")
    @JsonFormat(pattern = Constant.DATA_YMD_CN, timezone = "GMT+8")
    private Date endDate;

    @ApiModelProperty("已展示次数")
    private Long displayTimes;

    @ApiModelProperty("折线图数据集合")
    private List<DisplayTimesBrokenChartItem> items;
}
