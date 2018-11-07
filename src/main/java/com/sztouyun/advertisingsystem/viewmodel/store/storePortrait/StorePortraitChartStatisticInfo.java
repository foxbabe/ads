package com.sztouyun.advertisingsystem.viewmodel.store.storePortrait;

import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.model.store.StorePortraitEnum;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import com.sztouyun.advertisingsystem.utils.NumberFormatUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class StorePortraitChartStatisticInfo{

    @ApiModelProperty(value = "门店画像的类型")
    private Integer storePortraitType;

    @ApiModelProperty(value = "门店画像类型下的子类名称")
    private String itemName;

    @ApiModelProperty(value = "门店画像类型下的子类")
    private Integer itemValue;

    @ApiModelProperty(value = "门店数量")
    private Integer storeCount  = 0;

    @ApiModelProperty(value = "已使用门店")
    private Integer usedStoreCount = 0;

    @ApiModelProperty(value = "未使用门店")
    private Integer unUsedStoreCount = 0;

    @ApiModelProperty(value = "门店使用率")
    private String usedStoreCountRatio  = "";

    public String getItemName() {
        return EnumUtils.getDisplayName(itemValue, EnumUtils.toEnum(storePortraitType,StorePortraitEnum.class).getValueEnum());
    }

    public Integer getUnUsedStoreCount() {
        return storeCount-usedStoreCount;
    }

    public String getUsedStoreCountRatio() {
        return NumberFormatUtil.format(usedStoreCount.longValue(),storeCount.longValue(), Constant.RATIO_PATTERN);
    }
}
