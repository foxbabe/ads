package com.sztouyun.advertisingsystem.viewmodel.monitor;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel
@Data
public class PartnerAdvertisementMonitorDetailViewModel extends PartnerAdvertisementMonitorStatisticItem{
    @ApiModelProperty(value = "订单金额")
    private Integer orderAmount;

    @ApiModelProperty(value = "订单状态名称")
    private String orderStatusName;

    @ApiModelProperty(value = "投放结束时间")
    @JsonFormat(pattern = Constant.TIME_YMDHM_CN, timezone = "GMT+8")
    private Date effectiveEndTime;

    @ApiModelProperty(value = "实际投放天数")
    private Integer effectiveTotalDays;

    @ApiModelProperty(value = "监控开始时间")
    @JsonFormat(pattern = Constant.TIME_YMDHM_CN, timezone = "GMT+8")
    private Date monitorStartTime;

    @ApiModelProperty(value = "监控结束时间")
    @JsonFormat(pattern = Constant.TIME_YMDHM_CN, timezone = "GMT+8")
    private Date monitorEndTime;

    @ApiModelProperty(value = "监控周期")
    private Integer monitorPeriod;

    @ApiModelProperty(value = "合作方头像")
    private String portrait;
}
