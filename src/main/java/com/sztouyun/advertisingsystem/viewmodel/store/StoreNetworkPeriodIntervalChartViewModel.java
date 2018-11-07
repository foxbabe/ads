package com.sztouyun.advertisingsystem.viewmodel.store;

import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.utils.NumberFormatUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class StoreNetworkPeriodIntervalChartViewModel {

    @ApiModelProperty(value = "时段区间")
    private String interval;
    @ApiModelProperty(value = "门店总数")
    private Integer storeCount=0;
    @ApiModelProperty(value = "无网络门店数量")
    private Integer disNetworkStoreCount=0;
    @ApiModelProperty(value = "有网络门店数量")
    private Integer networkStoreCount=0;
    @ApiModelProperty(value = "4G门店数量")
    private Integer storeCount4G=0;
    @ApiModelProperty(value = "WIFI门店数量")
    private Integer storeCountWIFI=0;
    @ApiModelProperty(value = "无网络门店数量占比")
    private String disNetworkStoreCountRatio;
    @ApiModelProperty(value = "有网络门店数量占比")
    private String networkStoreCountRatio;
    @ApiModelProperty(value = "4G门店数量占比")
    private String storeCount4GRatio;
    @ApiModelProperty(value = "WIFI门店数量占比")
    private String storeCountWIFIRatio;

    public Integer getDisNetworkStoreCount() {
        return storeCount-networkStoreCount;
    }

    public String getDisNetworkStoreCountRatio() {
        return  NumberFormatUtil.format(getDisNetworkStoreCount().longValue(), storeCount.longValue(), Constant.RATIO_PATTERN);
    }

    public String getNetworkStoreCountRatio() {
        return NumberFormatUtil.format(networkStoreCount.longValue(), storeCount.longValue(), Constant.RATIO_PATTERN);
    }

    public String getStoreCount4GRatio() {
        return NumberFormatUtil.format(storeCount4G.longValue(), storeCount.longValue(), Constant.RATIO_PATTERN);
    }

    public String getStoreCountWIFIRatio() {
        return NumberFormatUtil.format(storeCountWIFI.longValue(), storeCount.longValue(), Constant.RATIO_PATTERN);
    }
}
