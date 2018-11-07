package com.sztouyun.advertisingsystem.viewmodel.contract;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

@ApiModel
public class ContractAdvertisementDetailPositionViewModel {

    @ApiModelProperty(value = "终端类型; 1:收银机; 2:IOS; 3:Android")
    private Integer terminalType;

    @ApiModelProperty(value = "终端类型名称")
    private String terminalTypeName;

    @ApiModelProperty(value = "对应详细的广告位置配置")
    private List<ContractAdvertisementPositionConfigItem> contractAdvertisementPositionConfigItems = new ArrayList<>();


    public List<ContractAdvertisementPositionConfigItem> getContractAdvertisementPositionConfigItems() {
        return contractAdvertisementPositionConfigItems;
    }

    public void setContractAdvertisementPositionConfigItems(List<ContractAdvertisementPositionConfigItem> contractAdvertisementPositionConfigItems) {
        this.contractAdvertisementPositionConfigItems = contractAdvertisementPositionConfigItems;
    }

    public Integer getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(Integer terminalType) {
        this.terminalType = terminalType;
    }

    public String getTerminalTypeName() {
        return terminalTypeName;
    }

    public void setTerminalTypeName(String terminalTypeName) {
        this.terminalTypeName = terminalTypeName;
    }
}
