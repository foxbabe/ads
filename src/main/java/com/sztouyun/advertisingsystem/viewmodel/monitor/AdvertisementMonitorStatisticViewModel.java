package com.sztouyun.advertisingsystem.viewmodel.monitor;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.utils.NumberFormatUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel
@Data
public class AdvertisementMonitorStatisticViewModel {

    @ApiModelProperty(value = "广告ID")
    private String id;

    @ApiModelProperty(value = "广告名称")
    private String advertisementName;

    @ApiModelProperty(value = "投放平台")
    private String platform;

    @ApiModelProperty(value = "投放城市数量")
    private Integer totalCityCount;

    @ApiModelProperty(value = "投放门店数量")
    private Integer totalStoreCount;

    @ApiModelProperty(value = "广告客户")
    private String customerName;

    @ApiModelProperty(value = "维护人")
    private String owner;

    @ApiModelProperty(value = "开始投放时间")
    @JsonFormat(pattern = Constant.TIME_YMDHM_CN, timezone = "GMT+8")
    private Date startTime;

    @ApiModelProperty(value = "结束投放时间")
    @JsonFormat(pattern = Constant.TIME_YMDHM_CN, timezone = "GMT+8")
    private Date endTime;

    @ApiModelProperty(value = "实际已投放天数")
    private String deliveryPeroid;

    @ApiModelProperty(value = "监控状态")
    private Integer status;

    @ApiModelProperty(value = "监控状态名称")
    private String statusName;

    @ApiModelProperty(value = "展示总数")
    private Long totalDisplayTimes;

    @ApiModelProperty(value = "已展示次数")
    private Long displayTimes;

    @ApiModelProperty(value = "完成比例")
    private String completeRatio;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = Constant.DATA_YMD_CN, timezone = "GMT+8")
    private Date updatedTime;

    @ApiModelProperty(value = "合同名称")
    private String contractName;

    @ApiModelProperty(value = "合同Id")
    private String contractId;

    @ApiModelProperty(value = "是否投放收银机")
    private boolean choseCashRegister;

    public String getCompleteRatio() {
        return NumberFormatUtil.format(displayTimes,totalDisplayTimes, Constant.RATIO_PATTERN);
    }
}
