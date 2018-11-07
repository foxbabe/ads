package com.sztouyun.advertisingsystem.viewmodel.adProfitShare;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class PeriodStoreProfitStatisticViewModel {
    @ApiModelProperty(value = "结算金额")
    private String settledAmount;

    @ApiModelProperty(value = "省份ID")
    private String provinceId;

    @ApiModelProperty(value = "城市ID")
    private String cityId;

    @ApiModelProperty(value = "地区ID")
    private String regionId;

    @ApiModelProperty(value = "门店名称")
    private String storeName;

    @ApiModelProperty(value = "门店ID")
    private String id;

    @ApiModelProperty(value = "门店shopId",hidden = true)
    private String shopId;

    @ApiModelProperty(value = "是否达标")
    private Boolean isQualified;

    @ApiModelProperty(value = "省份名称")
    private String provinceName;

    @ApiModelProperty(value = "城市名称")
    private String cityName;

    @ApiModelProperty(value = "地区名称")
    private String regionName;

    @ApiModelProperty(value = "设备ID")
    private String deviceId;

    @ApiModelProperty(value = "银行卡号")
    private String bankCard="";

    @ApiModelProperty(value = "可否查看")
    private Boolean canView;
}
