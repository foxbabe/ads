package com.sztouyun.advertisingsystem.viewmodel.store;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 *
 */
@ApiModel
@Data
public class StoreInfoViewModel extends BaseStoreInfoViewModel{
    @ApiModelProperty(value = "门店Id")
    private String id;

    @ApiModelProperty(value="已用广告位数量")
    private  Integer usedCount;

    @ApiModelProperty(value = "可用广告位数量")
    private Integer availableCount;

    @ApiModelProperty(value = "是否已选择(1:选中，0：未选中)")
    private Integer isChoose;

    @ApiModelProperty(value = "分成总额")
    private String totalShareAmount="0";

    @ApiModelProperty(value = "已结算总额")
    private String settledAmount="0";

    @ApiModelProperty(value = "未结算总额")
    private String unsettledAmount="0";

    @ApiModelProperty(value = "分成最小月份")
    @JsonFormat(pattern = Constant.DATA_YM_CN, timezone = "GMT+8")
    private Date shareAmountMinMonth;

    @ApiModelProperty(value = "分成最大月份")
    @JsonFormat(pattern = Constant.DATA_YM_CN, timezone = "GMT+8")
    private Date shareAmountMaxMonth;

    @ApiModelProperty(value = "是否有心跳 true: 有心跳  false: 没有心跳")
    private Boolean hasHeart;

    @ApiModelProperty(value = "可否查看")
    private Boolean canView;

    @ApiModelProperty(value = "是否铺货")
    private Boolean isPaveGoods;

    @ApiModelProperty(value = "是否达标")
    private Boolean isQualified;

}
