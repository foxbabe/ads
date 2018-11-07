package com.sztouyun.advertisingsystem.viewmodel.coorperationPartner;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel
@Data
public class PartnerAdvertisementDetailInfo extends PartnerAdvertisementBaseViewModel {

    @ApiModelProperty(value = "监控状态名称")
    private String statusName;

    @ApiModelProperty(value = "监控状态")
    private Integer status;

    @ApiModelProperty(value = "监控开始时间")
    @JsonFormat(pattern = Constant.DATE_TIME_CN, timezone = "GMT+8")
    private Date startTime;

    @ApiModelProperty(value = "监控结束时间")
    @JsonFormat(pattern = Constant.DATE_TIME_CN, timezone = "GMT+8")
    private Date endTime;

    @ApiModelProperty(value = "监控周期")
    private String monitorPeriod;


}
