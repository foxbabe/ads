package com.sztouyun.advertisingsystem.viewmodel.monitor;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.annotation.validation.EnumValue;
import com.sztouyun.advertisingsystem.model.common.MonitorStatusEnum;
import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel
@Data
public class PartnerAdvertisementMonitorStatisticRequest extends BasePageInfo{
    @ApiModelProperty(value = "订单名称")
    private String orderName;

    @ApiModelProperty(value = "订单编号")
    private String orderCode;

    @ApiModelProperty(value = "合作方ID, 选择 '全部' 则不要传入这个字段")
    private String partnerId;

    @ApiModelProperty(value = "开始投放时间")
    @JsonFormat(pattern = Constant.DATETIME, timezone = "GMT+8")
    private Date startTime;

    @ApiModelProperty(value = "结束投放时间")
    @JsonFormat(pattern = Constant.DATETIME, timezone = "GMT+8")
    private Date endTime;

    @ApiModelProperty(value = "监控状态(0:全部，1：监控中，2：已完成)",required = true)
    @EnumValue(enumClass = MonitorStatusEnum.class)
    private Integer status;
}
