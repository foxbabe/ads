package com.sztouyun.advertisingsystem.viewmodel.advertisement;

import com.sztouyun.advertisingsystem.model.system.TerminalTypeEnum;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wenfeng on 2017/12/13.
 */
@ApiModel
public class TerminalAdvertisementConfigInfo extends AbstractBaseAdvertisementConfigInfo {
    @ApiModelProperty(value = "终端类型")
    private Integer terminalType;
    @ApiModelProperty(value = "终端名称")
    private String terminalName;
    @ApiModelProperty(value = "终端对应广告配置列表")
    private List<AdvertisementDeliveryConfigInfo> deliveryConfigList;

    public TerminalAdvertisementConfigInfo(){
    }
    public TerminalAdvertisementConfigInfo(Integer terminalType){
        this.terminalType=terminalType;
        this.terminalName= EnumUtils.getDisplayName(terminalType,TerminalTypeEnum.class);
        deliveryConfigList=new ArrayList<>();
    }

    public Integer getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(Integer terminalType) {
        this.terminalType = terminalType;
    }

    public String getTerminalName() {
        return terminalName;
    }

    public void setTerminalName(String terminalName) {
        this.terminalName = terminalName;
    }

    public List<AdvertisementDeliveryConfigInfo> getDeliveryConfigList() {
        return deliveryConfigList;
    }

    public void setDeliveryConfigList(List<AdvertisementDeliveryConfigInfo> deliveryConfigList) {
        deliveryConfigList = deliveryConfigList;
    }
    @Override
    public boolean equals(Object other) {
        if(other == this)
            return true;
        if(!(other instanceof TerminalAdvertisementConfigInfo))
            return false;
        TerminalAdvertisementConfigInfo obj = (TerminalAdvertisementConfigInfo)other;
        return obj.getTerminalType().equals(terminalType);
    }
    @Override
    public int hashCode() {
        return terminalType.hashCode();
    }

}
