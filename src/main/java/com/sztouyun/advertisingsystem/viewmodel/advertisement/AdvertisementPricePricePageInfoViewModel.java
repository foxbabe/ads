package com.sztouyun.advertisingsystem.viewmodel.advertisement;

import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@ApiModel
public class AdvertisementPricePricePageInfoViewModel extends BasePageInfo {
    @ApiModelProperty(value = "门店类型门(全部:0,A类:1,B类:2 ,C类:3)", required = true)
    @NotNull(message = "门店类型不能为空")
    @Min(value = 0,message = "门店类型不匹配")
    @Max(value = 3,message = "门店类型不匹配")
    private Integer storeType;

    public Integer getStoreType() {
        return storeType;
    }

    public void setStoreType(Integer storeType) {
        this.storeType = storeType;
    }
}
