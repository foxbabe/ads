package com.sztouyun.advertisingsystem.viewmodel.customerStore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by szty on 2018/6/13.
 */
@ApiModel
@Data
public class InvalidCustomerStorePlanDetail {
    @ApiModelProperty(value = "门店ID")
    private String shopId;

    @ApiModelProperty(value = "门店名称")
    private String storeName;

    @ApiModelProperty(value = "城市")
    private String cityName;

    @ApiModelProperty(value = "地区")
    private String regionName;

    @ApiModelProperty(value = "具体地址")
    private String storeAddress;

    @ApiModelProperty(value = "设备编号")
    private String deviceId;

    @ApiModelProperty(value = "无效门店类型")
    private Integer customerStoreInvalidType;

    @ApiModelProperty(value = "备注")
    private String remark="";
}
