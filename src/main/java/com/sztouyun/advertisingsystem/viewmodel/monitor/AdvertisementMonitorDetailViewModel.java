package com.sztouyun.advertisingsystem.viewmodel.monitor;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel
@Data
public class AdvertisementMonitorDetailViewModel extends AdvertisementMonitorStatisticViewModel{
    @JsonFormat(pattern = Constant.TIME_YMDHM_CN, timezone = "GMT+8")
    @ApiModelProperty(value = "监控开始时间")
    private Date monitorStartTime;

    @ApiModelProperty(value = "监控结束时间")
    @JsonFormat(pattern = Constant.TIME_YMDHM_CN, timezone = "GMT+8")
    private Date monitorFinishedTime;

    @ApiModelProperty(value = "监控周期")
    private String monitorPeriod;

    @ApiModelProperty(value = "客户头像")
    private String headPortrait;

    @ApiModelProperty(value = "广告类型名称")
    private String advertisementTypeName;

    @ApiModelProperty(value = "广告状态名称")
    private String advertisementStatusName;

    @ApiModelProperty(value = "广告是否配置链接 - 广告无配置任何链接方式，相关统计数据不展示")
    private Boolean isConfiguredUrl;
}
