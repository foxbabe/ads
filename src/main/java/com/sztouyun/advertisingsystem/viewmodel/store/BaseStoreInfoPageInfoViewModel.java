package com.sztouyun.advertisingsystem.viewmodel.store;

import com.sztouyun.advertisingsystem.common.annotation.validation.EnumValue;
import com.sztouyun.advertisingsystem.model.store.StoreSourceEnum;
import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class BaseStoreInfoPageInfoViewModel extends BasePageInfo {
    @ApiModelProperty(value = "地区ID 字符串(以英文逗号间隔)")
    private String areaIds = "";

    @ApiModelProperty(value = "门店名称")
    private String storeName ="";

    @ApiModelProperty(value = "设备ID")
    private String deviceId ="";

    @ApiModelProperty(value = "门店ID")
    private String shopId ="";

    @ApiModelProperty(value ="门店的来源,全部:null, 1:运维门店 2:运维新门店")
    @EnumValue(enumClass = StoreSourceEnum.class,message = "门店来源不匹配",nullable = true)
    private Integer storeSource;

    @ApiModelProperty(value = "门店是否可用")
    private Boolean available;

    @ApiModelProperty(value = "门店是否达标")
    private Boolean isQualified;

    @ApiModelProperty(value = "经度")
    private Double longitude;

    @ApiModelProperty(value = "纬度")
    private Double latitude;

    @ApiModelProperty(value = "附近的距离长度, 单位千米")
    private Double distance;

    @ApiModelProperty(value = "是否使用地图")
    private Boolean isWithCoordinate=Boolean.FALSE;

}
