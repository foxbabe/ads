package com.sztouyun.advertisingsystem.viewmodel.monitor;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.annotation.validation.EnumValue;
import com.sztouyun.advertisingsystem.model.store.StoreSourceEnum;
import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel
@Data
public class PartnerOrderStoreMonitorRequest extends BasePageInfo {

    @ApiModelProperty(value = "订单ID")
    private String orderId;

    @ApiModelProperty(value = "全部:0, 1:已激活 2:未激活")
    private int status;

    @ApiModelProperty(value = "省ID")
    private String provinceId;

    @ApiModelProperty(value = "城市ID")
    private String cityId;

    @ApiModelProperty(value = "地区ID")
    private String regionId;

    @ApiModelProperty(value = "门店名称")
    private String storeName;

    @ApiModelProperty(value = "设备ID")
    private String deviceId;

    @ApiModelProperty(value = "门店ID")
    private String shopId;

    @ApiModelProperty(value ="门店的来源,全部:null, 1:运维门店 2:运维新门店")
    @EnumValue(enumClass = StoreSourceEnum.class,message = "门店来源不匹配",nullable = true)
    private Integer storeSource;

    @ApiModelProperty(value = "全部:0, 1:可用 2:不可用")
    private Integer available;

    @ApiModelProperty(value = "投放开始时间")
    @JsonFormat(pattern = Constant.DATA_YMD, timezone = "GMT+8")
    private Date startTime;

    @ApiModelProperty(value = "投放结束时间")
    @JsonFormat(pattern = Constant.DATA_YMD, timezone = "GMT+8")
    private Date endTime;

    @ApiModelProperty(hidden = true)
    private Date effectiveStartTime;

    @ApiModelProperty(hidden = true)
    private Date effectiveEndTime;



}
