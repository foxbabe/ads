package com.sztouyun.advertisingsystem.viewmodel.customer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by szty on 2017/7/27.
 */
@ApiModel
public class CustomerListItem extends CustomerViewModel {

    @ApiModelProperty(value = "维护人名称", required = true)
    private String ownerName = "";

    @ApiModelProperty(value = "城市名称", required = true)
    private String cityName = "";

    @ApiModelProperty(value = "正在投放广告")
    private boolean delivering = false;

    @ApiModelProperty(value = "是否创建账号")
    private boolean createAccount;

    @ApiModelProperty(value = "是否禁用账号")
    private boolean enable;

    @ApiModelProperty(value = "投放次数")
    private long advertisementDeliveryTimes = 0;

    @ApiModelProperty(value = "广告总额")
    private double advertisementTotalAmount = 0.00;

    public boolean isCreateAccount() {
        return createAccount;
    }

    public void setCreateAccount(boolean createAccount) {
        this.createAccount = createAccount;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public boolean isDelivering() {
        return delivering;
    }

    public void setDelivering(boolean delivering) {
        this.delivering = delivering;
    }

    public long getAdvertisementDeliveryTimes() {
        return advertisementDeliveryTimes;
    }

    public void setAdvertisementDeliveryTimes(long advertisementDeliveryTimes) {
        this.advertisementDeliveryTimes = advertisementDeliveryTimes;
    }

    public double getAdvertisementTotalAmount() {
        return advertisementTotalAmount;
    }

    public void setAdvertisementTotalAmount(double advertisementTotalAmount) {
        this.advertisementTotalAmount = advertisementTotalAmount;
    }
}
