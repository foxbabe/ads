package com.sztouyun.advertisingsystem.viewmodel.message.contract;

import com.sztouyun.advertisingsystem.viewmodel.message.MessageViewModel;
import com.sztouyun.advertisingsystem.viewmodel.message.advertisement.AdvertisementInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel
public class ContractMessageViewModel extends MessageViewModel {
    @ApiModelProperty("合同ID")
    private String contractId;
    @ApiModelProperty("合同名称")
    private String contractName;
    @ApiModelProperty(value = "关联的广告对象")
    private List<AdvertisementInfo> connectedAdvertisements;

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

    public List<AdvertisementInfo> getConnectedAdvertisements() {
        return connectedAdvertisements;
    }

    public void setConnectedAdvertisements(List<AdvertisementInfo> connectedAdvertisements) {
        this.connectedAdvertisements = connectedAdvertisements;
    }
}
