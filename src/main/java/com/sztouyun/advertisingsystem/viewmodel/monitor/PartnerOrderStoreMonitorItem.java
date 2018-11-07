package com.sztouyun.advertisingsystem.viewmodel.monitor;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel
@Data
public class PartnerOrderStoreMonitorItem {

    @ApiModelProperty(value = "门店表结构ID")
    private String storeId;

    @ApiModelProperty(value = "门店ID")
    private String storeNo;

    @ApiModelProperty(value = "门店名称")
    private String storeName;

    @ApiModelProperty(value = "省份")
    private String provinceName;

    @ApiModelProperty(value = "城市")
    private String cityName;

    @ApiModelProperty(value = "地区")
    private String regionName;

    @ApiModelProperty(value = "具体地址")
    private String storeAddress;

    @ApiModelProperty(value = "设备ID")
    private String deviceId;

    @ApiModelProperty(value = "门店是否可用")
    private Boolean available= Boolean.FALSE;

    @ApiModelProperty(value = "是否激活;  是:true 不是:false")
    private Boolean active = Boolean.FALSE;

    @ApiModelProperty(value = "已展示次数")
    private Integer displayTimes;

    @ApiModelProperty(value = "广告是否展示")
    private Boolean isDisplay= Boolean.FALSE;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = Constant.DATA_YMD_CN, timezone = "GMT+8")
    private Date updatedTime;

    @ApiModelProperty(value = "省份ID")
    private String provinceId;

    @ApiModelProperty(value = "城市ID")
    private String cityId;

    @ApiModelProperty(value = "地区ID")
    private String regionId;


}
