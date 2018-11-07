package com.sztouyun.advertisingsystem.viewmodel.partner.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class StoreInfoRequest {

    @ApiModelProperty(name = "操作系统主版本号")
    private String osVersion;

    @ApiModelProperty(name = "设备厂商")
    private String vendor;

    @ApiModelProperty(name = "机型")
    private String model;

    @ApiModelProperty(name = "网络类型  0:未知网络, 1:WIFI, 2:2G, 3:3G, 4:4G, 101:以太网接入, 999:未知新类型")
    private Integer connectionType;

    @ApiModelProperty(name = "门店编号")
    private String storeNo;

    @ApiModelProperty(name = "合作方ID", hidden = true)
    private String partnerId;

    @ApiModelProperty(name = "请求IP")
    private String requestIp;

    @ApiModelProperty(name = "门店广告位类型", hidden = true)
    private Integer advertisementPositionCategory;

    @ApiModelProperty(name = "门店ID", hidden = true)
    private String storeId;

    @ApiModelProperty(name = "门店mac地址")
    private String mac;
}
