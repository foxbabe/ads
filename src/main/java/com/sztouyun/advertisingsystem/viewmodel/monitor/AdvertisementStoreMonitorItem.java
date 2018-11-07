package com.sztouyun.advertisingsystem.viewmodel.monitor;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel
public class AdvertisementStoreMonitorItem {
    @ApiModelProperty(value = "门店ID")
    private String id;

    @ApiModelProperty(value = "门店名称")
    private String storeName;

    @ApiModelProperty(value = "门店ID")
    private String shopId;

    @ApiModelProperty(value = "门店所属省")
    private String provinceName;

    @ApiModelProperty(value = "门店所属城市")
    private String cityName;

    @ApiModelProperty(value = "门店所属地区")
    private String regionName;

    @ApiModelProperty(value = "门店具体地址")
    private String storeAddress;

    @ApiModelProperty(value = "设备ID")
    private String deviceId;

    @ApiModelProperty(value = "是否激活")
    private Boolean isActive;

    @ApiModelProperty(value = "展示次数")
    private Long displayTimes;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = Constant.DATA_YMD_CN, timezone = "GMT+8")
    private Date updatedTime;

    @ApiModelProperty(hidden = true)
    private String provinceId;

    @ApiModelProperty(hidden = true)
    private String cityId;

    @ApiModelProperty(hidden = true)
    private String regionId;

    @ApiModelProperty(value = "门店是否可用")
    private Boolean available;

    @ApiModelProperty(value = "可否查看")
    private Boolean canView;

    @ApiModelProperty(value = "是否达标")
    private Boolean isQualified;
}
