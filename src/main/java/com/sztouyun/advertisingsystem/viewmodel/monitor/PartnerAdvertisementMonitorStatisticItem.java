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
public class PartnerAdvertisementMonitorStatisticItem{

    @ApiModelProperty(value = "监控ID")
    private String id;

    @ApiModelProperty(value = "订单编号")
    private String code;

    @ApiModelProperty(value = "订单名称")
    private String name;

    @ApiModelProperty(value = "合作方名称")
    private String partnerName;

    @ApiModelProperty(value = "订单总门店数")
    private Integer totalStoreCount;

    @ApiModelProperty(value = "投放位置描述")
    private String advertisementPositionTypeName; //没有

    @ApiModelProperty(value = "监控状态名称")
    private String statusName;

    @ApiModelProperty(value = "开始投放时间")
    @JsonFormat(pattern = Constant.TIME_YMDHM_CN, timezone = "GMT+8")
    private Date effectiveStartTime;

    @ApiModelProperty(value = "预计总展示次数")
    private Integer totalDisplayTimes;

    @ApiModelProperty(value = "实际展示次数")
    private Integer displayTimes;

    @ApiModelProperty(value = "完成比例")
    private String ratio;

    @JsonFormat(pattern = Constant.DATA_YMD_CN, timezone = "GMT+8")
    private Date createdTime;
}
