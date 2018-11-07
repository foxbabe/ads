package com.sztouyun.advertisingsystem.viewmodel.adProfitShare;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * Created by wenfeng on 2018/1/18.
 */
@Data
@ApiModel
public class BaseStoreProfit {
    @ApiModelProperty(value = "门店月流水ID")
    private String id;

    @ApiModelProperty(value = "门店名称")
    private String storeName;

    @ApiModelProperty(value = "门店ID")
    private String storeId;

    @ApiModelProperty(value = "具体地址")
    private String storeAddress;

    @ApiModelProperty(value = "设备ID")
    private String deviceId;

    @ApiModelProperty(value = "是否达标")
    private Boolean isQualified;

    @JsonFormat(pattern = Constant.DATA_YM_CN, timezone = "GMT+8")
    @ApiModelProperty(value = "结算月份")
    private Date settledMonth;

    @JsonFormat(pattern = Constant.DATA_YM_CN, timezone = "GMT+8")
    @ApiModelProperty(value = "最小结算月份")
    private Date minSettledMonth;

    @JsonFormat(pattern = Constant.DATA_YM_CN, timezone = "GMT+8")
    @ApiModelProperty(value = "最大结算月份")
    private Date maxSettledMonth;

    @JsonFormat(pattern = Constant.DATA_YMD_CN, timezone = "GMT+8")
    @ApiModelProperty(value = "分成时间")
    private Date profitDate;

    @ApiModelProperty(value = "分成金额")
    private String shareAmount;

    @ApiModelProperty(value = "分成结算")
    private Boolean settled=Boolean.FALSE;

    @ApiModelProperty(value = "门店系统的门店ID")
    private String shopId;

    @ApiModelProperty(value = "是否选中")
    private Boolean checked;

    @ApiModelProperty(value = "门店是否有多条流水")
    private Boolean multiRecords=Boolean.FALSE;
}
