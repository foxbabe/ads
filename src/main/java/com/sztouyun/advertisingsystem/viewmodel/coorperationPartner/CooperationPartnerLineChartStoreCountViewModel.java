package com.sztouyun.advertisingsystem.viewmodel.coorperationPartner;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@ApiModel
@Data
public class CooperationPartnerLineChartStoreCountViewModel {

    @ApiModelProperty(value = "每天的请求门店数量信息")
    private List<CooperationPartnerLineChartStoreCountInfo> cooperationPartnerLineChartStoreCountInfos = new ArrayList<>();

    @ApiModelProperty(value = "门店资源数量")
    private Integer configStoreCount=0;

    @ApiModelProperty(value = "周期内请求门店数量")
    private Integer periodRequestStoreCount=0;

    @ApiModelProperty(value = "周期内未请求门店数量")
    private Integer periodUnrequestedStoreCount=0;

    public Integer getPeriodUnrequestedStoreCount() {
        return configStoreCount-periodRequestStoreCount;
    }
}
