package com.sztouyun.advertisingsystem.model.mongodb.profit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.IntegerSpan;
import com.sztouyun.advertisingsystem.common.annotation.validation.EnumValue;
import com.sztouyun.advertisingsystem.common.annotation.validation.ValidDataTable;
import com.sztouyun.advertisingsystem.model.store.StoreInfo;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.var;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdvertisementProfitConfig {
    @ApiModelProperty(value = "广告ID",hidden = true)
    private String id;

    @EnumValue(enumClass = AdvertisementProfitModeEnum.class, message = "分成模式枚举值不正确")
    @ApiModelProperty("分成模式: 1 - 投放效果计费, 2 - 投放点计费, 3 - 活跃度计费")
    private Integer mode;

    @Valid
    @ApiModelProperty("投放效果计费分成配置")
    private AdvertisementDeliveryEffectProfitConfig deliveryEffectProfitConfig;

    @Valid
    @ApiModelProperty("投放点计费分成配置")
    private AdvertisementDeliveryPointProfitConfig deliveryPointProfitConfig;

    @ValidDataTable
    @ApiModelProperty("活跃度计费分成配置")
    private AdvertisementActiveDegreeProfitConfig activeDegreeProfitConfig;

    public AdvertisementProfitModeEnum getAdvertisementProfitModeEnum() {
        return EnumUtils.toEnum(mode, AdvertisementProfitModeEnum.class);
    }

    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private HashMap<String,Integer> bootTimeStandardMap;
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private HashMap<String,HashMap<Integer,Double>> shareAmountMap;
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private Double storeBootTimeProfitAmount;

    /**
     * 获取广告下面门店的开机标准
     */
    public Integer getBootTimeStandard(StoreInfo storeInfo){
        var  profitMode =getAdvertisementProfitModeEnum();
        if(profitMode.equals(AdvertisementProfitModeEnum.DeliveryEffect))
            return 0;
        if(profitMode.equals(AdvertisementProfitModeEnum.DeliveryPoint))
            return deliveryPointProfitConfig.getBootHour();
        Integer bootTimeStandard =bootTimeStandardMap.get(storeInfo.getCityId());
        if(bootTimeStandard == null)
        {
            bootTimeStandard =bootTimeStandardMap.getOrDefault(Constant.OTHER_CITY_ID,0);
        }
        return bootTimeStandard;
    }

    public void init(){
        var  profitMode =getAdvertisementProfitModeEnum();
        if(!AdvertisementProfitModeEnum.ActiveDegree.equals(profitMode))
            return;

        bootTimeStandardMap = new HashMap<>();
        shareAmountMap = new HashMap<>();
        for (var row: activeDegreeProfitConfig.getRows()){
            Integer bootTimeStandard =0;
            HashMap<Integer,Double> shareAmountRowMap = new HashMap<>();
            Set<String> cityIds =new HashSet<>();
            for (var cell:row.getCells()){
                if(AdvertisementActiveDegreeProfitConfigCellType.AreaList.getValue().equals(cell.getType())){
                    Map<String,String> areaMap = cell.getData();
                    cityIds = areaMap.keySet();
                }else if(AdvertisementActiveDegreeProfitConfigCellType.StoreMinBootTime.getValue().equals(cell.getType())){
                    bootTimeStandard = cell.getData();
                }else if(AdvertisementActiveDegreeProfitConfigCellType.StoreOrderProfitAmount.getValue().equals(cell.getType())){
                    shareAmountRowMap.put(cell.getCellIndex(),cell.findDoubleData());
                }else if(AdvertisementActiveDegreeProfitConfigCellType.StoreBootTimeProfitAmount.getValue().equals(cell.getType())){
                    storeBootTimeProfitAmount = cell.findDoubleData();
                }
            }
            for (var cityId :cityIds){
                bootTimeStandardMap.put(cityId,bootTimeStandard);
                if(!Constant.OTHER_CITY_ID.equals(cityId)){
                    shareAmountMap.put(cityId,shareAmountRowMap);
                }
            }
        }
    }

    /**
     * 获取广告下门店的分成金额
     * @return
     */
    public Long getShareAmount(StoreInfo storeInfo,int orderCount){
        var  profitMode =getAdvertisementProfitModeEnum();
        if(profitMode.equals(AdvertisementProfitModeEnum.DeliveryEffect))
            return 0L;
        if(profitMode.equals(AdvertisementProfitModeEnum.DeliveryPoint))
            return toCent(deliveryPointProfitConfig.getDailyAmount());

        HashMap<Integer,Double> shareAmountRowMap = shareAmountMap.get(storeInfo.getCityId());
        if(shareAmountRowMap ==null)
            return toCent(storeBootTimeProfitAmount);
        var cellIndex = getMatchedOrderCountCellIndex(orderCount);
        if(cellIndex == null)
            return 0L;
        double shareAmount = shareAmountRowMap.getOrDefault(cellIndex,0D);
        return toCent(shareAmount);
    }

    private Long toCent(Double amount){
        if(amount == null)
            return 0L;
        amount = amount *100;
        return amount.longValue();
    }

    private Integer getMatchedOrderCountCellIndex(int orderCount){
        var row = activeDegreeProfitConfig.getRows().get(0);
        for (var cell:row.getCells()){
            if(!AdvertisementActiveDegreeProfitConfigCellType.StoreOrderSpan.getValue().equals(cell.getType()))
                continue;
            IntegerSpan span = cell.getData();
            if(orderCount>=span.getStartValue() && (span.getEndValue() ==null || orderCount<= span.getEndValue()))
                return cell.getCellIndex();
        }
        return null;
    }
}
