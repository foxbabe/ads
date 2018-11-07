package com.sztouyun.advertisingsystem.viewmodel.store;

import com.sztouyun.advertisingsystem.common.annotation.validation.EnumValue;
import com.sztouyun.advertisingsystem.model.store.StoreSourceEnum;
import com.sztouyun.advertisingsystem.viewmodel.monitor.ContractStoreInfoQueryRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class ContractStoreQueryRequest extends ContractStoreInfoQueryRequest {
    @ApiModelProperty(value = "当前广告ID")
    private String advertisementId="";
    @ApiModelProperty(value = "门店名称")
    private String storeName ="";
    @ApiModelProperty(value = "设备ID")
    private String deviceId ="";
    @ApiModelProperty(value = "门店shopID")
    private String shopId="";
    @ApiModelProperty(value = "门店ID",hidden = true)
    private String storeId="";
    @ApiModelProperty(value ="门店的来源,全部:null, 1:运维门店 2:运维新门店")
    @EnumValue(enumClass = StoreSourceEnum.class,message = "门店来源不匹配",nullable = true)
    private Integer storeSource;
    @ApiModelProperty(value = "是否达标")
    private Boolean isQualified;
}
