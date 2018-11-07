package com.sztouyun.advertisingsystem.viewmodel.advertisement;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Created by wenfeng on 2017/9/8.
 */
@Data
@ApiModel
public class DetailAdvertisementViewModel extends UpdateAdvertisementViewModel {
    @ApiModelProperty(value = "客户ID")
    private String customerId;

    @ApiModelProperty(value = "客户头像")
    private String portrait;

    @ApiModelProperty(value = "广告创建时间")
    @JsonFormat(pattern = Constant.DATA_YMD_CN, timezone = "GMT+8")
    private Date createdTime;

    @ApiModelProperty(value = "广告状态")
    private Integer advertisementStatus;

    @ApiModelProperty(value = "广告状态名称")
    private String advertisementStatusName;

    @ApiModelProperty(value = "广告创建人")
    private String creator;

    @ApiModelProperty(value = "广告客户名称")
    private String customerName;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "广告合同名称")
    private String contractName;

    @ApiModelProperty(value = "广告总价")
    private Double totalCost;

    @ApiModelProperty(value = "是否折扣")
    private Integer isDiscount = 0;

    @ApiModelProperty(value = "折扣比例")
    private Double discount;

    @ApiModelProperty(value = "分成金额")
    private String shareAmount;

    @ApiModelProperty(value = "已结算金额")
    private String settledAmount;

    @ApiModelProperty(value = "未结算金额")
    private String unsettledAmount;

    @ApiModelProperty(value = "维护人")
    private String owner;

    @ApiModelProperty(value = "合同周期开始时间")
    @JsonFormat(pattern = Constant.DATA_YMD_CN, timezone = "GMT+8")
    private Date startTimeOfContract;

    @ApiModelProperty(value = "合同周期结束时间")
    @JsonFormat(pattern = Constant.DATA_YMD_CN, timezone = "GMT+8")
    private Date endTimeOfContract;

    @ApiModelProperty(value = "合同周期")
    private String contractPeriod;

    @ApiModelProperty(value = "合同已投放周期")
    private String usedContractPeriod;

    @ApiModelProperty(value = "广告投放周期")
    private String advertisementPeriod;

    @ApiModelProperty(value = "广告类型名称")
    private String advertisementTypeName;

    @ApiModelProperty(value = "广告开始投放时间")
    @JsonFormat(pattern = Constant.TIME_YMDHM_CN, timezone = "GMT+8")
    private Date effectiveStartTime;

    @ApiModelProperty(value = "广告结束投放时间")
    @JsonFormat(pattern = Constant.TIME_YMDHM_CN, timezone = "GMT+8")
    private Date effectiveEndTime;

    @ApiModelProperty(value = "广告下架时间")
    @JsonFormat(pattern = Constant.TIME_YMDHM_CN, timezone = "GMT+8")
    private Date takeOffTime;

    @ApiModelProperty(value = "投放人")
    private String deliveryOperator;

    @ApiModelProperty(value = "下架人")
    private String takeOffOperator;

    @ApiModelProperty(value = "下架备注")
    private String takeOffRemark;

    @ApiModelProperty(value = "广告开始时间")
    @JsonFormat(pattern = Constant.TIME_YMDHM_CN, timezone = "GMT+8")
    private Date startTime;

    @ApiModelProperty(value = "广告截止时间")
    @JsonFormat(pattern = Constant.TIME_YMDHM_CN, timezone = "GMT+8")
    private Date endTime;

    @ApiModelProperty(value = "素材列表")
    private List<TerminalAdvertisementConfigInfo> materialItems;

    @ApiModelProperty(value = "更新时间", required = true)
    @JsonFormat(pattern = Constant.DATA_YMD_CN, timezone = "GMT+8")
    private Date updatedTime;

    @ApiModelProperty(value = "实际已投放天数")
    private String period;

    @ApiModelProperty(value = "分成模式: 0 - 固定分成计费, 1 - 投放效果计费, 2 - 投放点计费, 3 - 活跃度计费")
    private Integer mode;
}
