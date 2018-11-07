package com.sztouyun.advertisingsystem.viewmodel.storeDevice;

import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.utils.NumberFormatUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by szty on 2018/8/8.
 */
@Data
@ApiModel
@NoArgsConstructor
@AllArgsConstructor
public class StoreDeviceOpeningDurationInfo {
    @ApiModelProperty(value = "时间段")
    private String durationType;
    @ApiModelProperty(value = "符合时长的门店数量")
    private Integer storeCount=0;
    @ApiModelProperty(value = "门店总数")
    private Integer totalStoreCount;
    public String getRatio(){
        if(storeCount.equals(totalStoreCount))
            return Constant.MAX_PERCENT;
        return storeCount>0 ? NumberFormatUtil.format(storeCount.longValue(),totalStoreCount.longValue(), Constant.RATIO_PATTERN):Constant.ZERO_PERCENT;
    }

    public StoreDeviceOpeningDurationInfo(String durationType, Integer totalStoreCount) {
        this.durationType = durationType;
        this.totalStoreCount = totalStoreCount;
    }

    public void updateStoreCount(Integer updateCount){
        this.storeCount+=updateCount;
    }

}
