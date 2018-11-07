package com.sztouyun.advertisingsystem.viewmodel.message.advertisement;

import com.sztouyun.advertisingsystem.model.message.MessageTypeEnum;
import com.sztouyun.advertisingsystem.viewmodel.message.MessageViewModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class AdvertisementMessageViewModel extends MessageViewModel {
    @ApiModelProperty(value = "合同Id")
    private String contractId;
    @ApiModelProperty(value = "合同名称")
    private String contractName;
    @ApiModelProperty(value = "当前广告Id")
    private String advertisementId;
    @ApiModelProperty(value = "当前广告名称")
    private String advertisementName;
    @ApiModelProperty(value = "广告周期是否小于合同周期，true：是")
    private boolean effectivePeriod;
    @ApiModelProperty(value = "是否影响其他广告")
    private boolean affectOtherAdvertisements;

    public boolean isAffectOtherAdvertisements() {
        return affectOtherAdvertisements;
    }

    public void setAffectOtherAdvertisements(boolean affectOtherAdvertisements) {
        this.affectOtherAdvertisements = affectOtherAdvertisements;
    }

    public boolean isEffectivePeriod() {
        return effectivePeriod;
    }

    public void setEffectivePeriod(boolean effectivePeriod) {
        this.effectivePeriod = effectivePeriod;
    }

    public String getAdvertisementId() {
        return advertisementId;
    }

    public void setAdvertisementId(String advertisementId) {
        this.advertisementId = advertisementId;
    }

    public String getAdvertisementName() {
        return advertisementName;
    }

    public void setAdvertisementName(String advertisementName) {
        this.advertisementName = advertisementName;
    }

    public AdvertisementMessageViewModel() {
        setMessageType(MessageTypeEnum.Advertisement.getValue());
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }
}
