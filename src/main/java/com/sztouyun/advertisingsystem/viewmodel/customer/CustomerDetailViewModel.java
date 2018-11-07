package com.sztouyun.advertisingsystem.viewmodel.customer;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * Created by szty on 2017/7/25.
 */
@ApiModel
@Data
public class CustomerDetailViewModel extends UpdateCustomerViewModel{

    @ApiModelProperty(value = "所在省份")
    private String provinceName;

    @ApiModelProperty(value = "所在城市")
    private String cityName;

    @ApiModelProperty(value = "地区名称")
    private String regionName;

    @ApiModelProperty(value = "负责人名称")
    private String ownerName;

    @ApiModelProperty(value = "正在投放广告")
    private boolean delivering;

    @ApiModelProperty(value = "投放次数")
    private long advertisementDeliveryTimes = 0;

    @ApiModelProperty(value = "广告总额")
    private double advertisementTotalAmount = 0.00;

    @ApiModelProperty(value = "最后投放广告时间")
    @JsonFormat(pattern = Constant.TIME_YMDHM_CN, timezone = "GMT+8")
    private Date lastestDeliveryTime;

    @ApiModelProperty(value = "客户创建人")
    private String creator;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = Constant.DATA_YMD_CN,timezone = "GMT+8")
    private Date createdTime;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = Constant.DATA_YMD_CN,timezone = "GMT+8")
    private Date updatedTime;

    @ApiModelProperty(value = "省/市/区")
    private String region;

    @ApiModelProperty(value = "拜访次数")
    private long visitTimes;

    @ApiModelProperty(value = "最后一次拜访时间")
    @JsonFormat(pattern = Constant.DATA_YMD_CN, timezone = "GMT+8")
    private Date lastestVisitTime;

    @ApiModelProperty(value = "是否创建账号")
    private boolean createAccount;

    @ApiModelProperty(value = "是否禁用账号")
    private boolean enable;

    @ApiModelProperty(value = "从属行业标签")
    private String industryFlag;

    @ApiModelProperty(value = "从属子行业标签")
    private String subIndustryFlag;

}
