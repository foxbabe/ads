package com.sztouyun.advertisingsystem.viewmodel.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 */
@ApiModel
@Data
public class BaseStoreInfoViewModel {

    @ApiModelProperty(value = "省份ID")
    private String provinceId;

    @ApiModelProperty(value = "省code")
    private String provinceCode;

    @ApiModelProperty(value = "城市ID")
    private String cityId;

    @ApiModelProperty(value = "城市code")
    private String cityCode;

    @ApiModelProperty(value = "地区ID")
    private String regionId;

    @ApiModelProperty(value = "地区code")
    private String regionCode;

    @ApiModelProperty(value = "门店名称")
    private String storeName;

    @ApiModelProperty(value = "门店ID")
    private String shopId;

    @ApiModelProperty(value = "省份名称")
    private String provinceName;

    @ApiModelProperty(value = "城市名称")
    private String cityName;

    @ApiModelProperty(value = "地区名称")
    private String regionName;

    @ApiModelProperty(value = "具体地址")
    private String storeAddress;

    @ApiModelProperty(value = "设备ID")
    private String deviceId;

    @ApiModelProperty(value = "门店是否可用")
    private Boolean available;

    @ApiModelProperty(value = "是否激活;  是:true 不是:false")
    private Boolean active;

    @ApiModelProperty(value="是否使用; 使用:true, 未使用:false")
    private Boolean used;

    @ApiModelProperty(value = "经度")
    private Double longitude;

    @ApiModelProperty(value = "纬度")
    private double latitude;

    @ApiModelProperty(value = "门店是否达标")
    private Boolean isQualified;
}
