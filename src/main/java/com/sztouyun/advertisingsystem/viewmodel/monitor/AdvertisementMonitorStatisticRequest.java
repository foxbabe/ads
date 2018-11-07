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
public class AdvertisementMonitorStatisticRequest extends BasePageInfo {

    @ApiModelProperty(value = "投放平台(null:全部，0:全平台，1：收银机，2：iOS，3：Android)")
    private Integer platform;

    @ApiModelProperty(value = "广告名称")
    private String advertisementName;

    @ApiModelProperty(value = "广告合同名称")
    private String contractName;

    @ApiModelProperty(value = "广告客户")
    private String customerName;

    @ApiModelProperty(value = "业务员名称")
    private String nickname;

    @ApiModelProperty(value = "开始投放时间")
    @JsonFormat(pattern = Constant.DATETIME, timezone = "GMT+8")
    private Date startTime;

    @ApiModelProperty(value = "结束投放时间")
    @JsonFormat(pattern = Constant.DATETIME, timezone = "GMT+8")
    private Date endTime;

    @ApiModelProperty(value = "监控状态(0:全部，1：监控中，2：已完成)",required = true)
    @EnumValue(enumClass = MonitorStatusEnum.class,nullable = true)
    private Integer status;
}
