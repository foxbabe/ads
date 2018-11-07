package com.sztouyun.advertisingsystem.viewmodel.store.storePortrait;

import com.sztouyun.advertisingsystem.model.store.*;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class StorePortraitListViewModel {
    @ApiModelProperty(value = "门店ID")
    private String id;

    @ApiModelProperty(value = "门店名称")
    private String storeName;

    @ApiModelProperty(value = "门店ShopID")
    private String shopId;

    @ApiModelProperty(value = "省份名称")
    private String provinceName;

    @ApiModelProperty(value = "城市名称")
    private String cityName;

    @ApiModelProperty(value = "地区名称")
    private String regionName;

    @ApiModelProperty(value = "具体地址")
    private String storeAddress;

    @ApiModelProperty(value = "省份ID")
    private String provinceId;

    @ApiModelProperty(value = "城市ID")
    private String cityId;

    @ApiModelProperty(value = "地区ID")
    private String regionId;

    @ApiModelProperty(value = "门店类型")
    private int storeFrontType;

    @ApiModelProperty(value = "日销售额")
    private int dailySales;

    @ApiModelProperty(value = "装修情况")
    private int decoration;

    @ApiModelProperty(value = "周边环境")
    private String surroundingsDistrict;

    @ApiModelProperty(value = "营业面积")
    private int commercialArea;

    @ApiModelProperty(value = "订货比例")
    private int orderRatio;

    @ApiModelProperty(value = "能否查看")
    private boolean canView;

    public String getStoreFrontType() {
        return EnumUtils.getDisplayName(storeFrontType, StoreFrontTypeEnum.class);
    }

    public String getDailySales() {
        return EnumUtils.getDisplayName(dailySales, DailySalesEnum.class);
    }

    public String getDecoration() {
        return EnumUtils.getDisplayName(decoration, DecorationEnum.class);
    }

    public String getCommercialArea() {
        return EnumUtils.getDisplayName(commercialArea, CommercialAreaEnum.class);
    }

    public String getOrderRatio() {
        return EnumUtils.getDisplayName(orderRatio, OrderRatioEnum.class);
    }

}
