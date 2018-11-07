package com.sztouyun.advertisingsystem.viewmodel.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 *
 */
@ApiModel
@Data
public class StoreInfoMonthList extends BaseStoreInfoViewModel{

    @ApiModelProperty(value = "门店Id")
    private String id;

    @ApiModelProperty(value = "分成金额")
    private Double shareAmount;

    @ApiModelProperty(value = "分成月份")
    private Date shareAmountMonth;

    @ApiModelProperty(value = "是否结算")
    private Boolean settled;

    @ApiModelProperty(value = "可否查看")
    private Boolean canView;

}
