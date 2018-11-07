package com.sztouyun.advertisingsystem.viewmodel.monitor;

import com.sztouyun.advertisingsystem.common.annotation.validation.EnumValue;
import com.sztouyun.advertisingsystem.model.store.StoreSourceEnum;
import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@ApiModel
@Data
public class AdvertisementStoreMonitorItemRequest extends BasePageInfo {
    @ApiModelProperty(value = "广告ID", required = true)
    @NotBlank(message = "广告ID不能为空")
    private String advertisementId;

    @ApiModelProperty(value = "省份ID")
    private String provinceId;

    @ApiModelProperty(value = "城市ID")
    private String cityId;

    @ApiModelProperty(value = "地区ID")
    private String regionId;

    @ApiModelProperty(value = "是否激活; 全部:null, 是:1 否:2")
    private Boolean isActive;

    @ApiModelProperty(value = "门店名称")
    private String storeName;

    @ApiModelProperty(value = "设备ID")
    private String deviceId;

    @ApiModelProperty(value = "门店ID")
    private String shopId;

    @ApiModelProperty(value = " 全部: null, 可用: 1 不可用: 0")
    private Boolean available;

    @ApiModelProperty(value ="门店的来源,全部:null, 1:运维门店 2:运维新门店")
    @EnumValue(enumClass = StoreSourceEnum.class,message = "门店来源不匹配",nullable = true)
    private Integer storeSource;

    @ApiModelProperty(value = "是否达标")
    private Boolean isQualified;
}
