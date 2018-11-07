package com.sztouyun.advertisingsystem.viewmodel.advertisement;


import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.annotation.validation.EnumValue;
import com.sztouyun.advertisingsystem.model.contract.StoreTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created by wenfeng on 2017/8/4.
 */
@ApiModel
public class BaseAdvertisementPriceConfigViewModel {
    @ApiModelProperty(value = "门店类型(A类:1，B类:2 ,C类:3,D类：4)", required = true)
    @EnumValue(enumClass = StoreTypeEnum.class,nullable = false,message = "门店类型不匹配")
    private Integer storeType;


    @ApiModelProperty(value = "时间周期", required = true)
    @Max(value= Constant.INTEGER_MAX,message = "时间周期最大值不能超过999999999")
    @NotNull(message = "时间周期不能为空")
    @Min(value = 1,message = "时间周期不存在")
    private Integer  period;

    @ApiModelProperty(value = "价格", required = true)
    @Max(value= Constant.INTEGER_MAX,message = "价格最大值不能超过999999999")
    @NotNull(message = "价格不能为空")
    @Min(value = 0,message = "价格不能小于0")
    private Double price;

    public Integer getStoreType() {
        return storeType;
    }

    public void setStoreType(Integer storeType) {
        this.storeType = storeType;
    }


    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
