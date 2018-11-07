package com.sztouyun.advertisingsystem.viewmodel.store.storePortrait;

import com.sztouyun.advertisingsystem.model.store.StorePortraitEnum;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@ApiModel
public class StorePortraitChartStatisticViewModel {

    @ApiModelProperty(value = "门店画像的类型")
    private Integer storePortraitType;

    @ApiModelProperty(value = "门店画像的名称")
    private String storePortraitName;

    @ApiModelProperty(value = "门店画像的信息")
    private List<StorePortraitChartStatisticInfo> storePortraitChartStatisticInfos = new ArrayList<>();

    public String getStorePortraitName() {
        return EnumUtils.getDisplayName(storePortraitType, StorePortraitEnum.class);
    }

}
