package com.sztouyun.advertisingsystem.viewmodel.adProfitShare;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.viewmodel.AreaRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel
public class PeriodStoreProfitStatisticRequest extends AreaRequest {
    @ApiModelProperty(value = "结算表ID")
    private String settledStoreProfitId;
    @ApiModelProperty(value = "门店名称")
    private String storeName;
    @ApiModelProperty(value = "设备ID")
    private String deviceId;
    @ApiModelProperty(value = "门店ID")
    private String shopId;
    @ApiModelProperty(value="开始时间")
    @JsonFormat(pattern = Constant.DATA_YM, timezone = "GMT+8")
    private Date startTime;
    @ApiModelProperty(value="结束时间")
    @JsonFormat(pattern = Constant.DATA_YM, timezone = "GMT+8")
    private Date endTime;
    @ApiModelProperty(value = "是否结算，全部：null,是：true,否：false")
    private Boolean settled;
    @ApiModelProperty(value = "是否达标")
    private Boolean isQualified;
}
