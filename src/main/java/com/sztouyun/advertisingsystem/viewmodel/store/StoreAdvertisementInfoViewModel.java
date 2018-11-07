package com.sztouyun.advertisingsystem.viewmodel.store;

import com.sztouyun.advertisingsystem.model.store.*;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 */
@ApiModel
@Data
public class StoreAdvertisementInfoViewModel extends BaseStoreInfoViewModel {

    @ApiModelProperty(value = "广告总数")
    private Integer advertisementTotalCount;

    @ApiModelProperty(value = "已激活展示广告数")
    private Integer advertisementActiveCount;

    @ApiModelProperty(value = "门店广告激活率")
    private String advertisementActiveRatio;

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

    @ApiModelProperty(value = "盈利情况")
    private String profitability;

    @ApiModelProperty(value = "银行卡号")
    private String bankCard;

    @ApiModelProperty(value = "居民区数量")
    private Integer residentialDistrictCount;

    @ApiModelProperty(value = "门头照")
    private String outsidePicture;

    @ApiModelProperty(value = "店内照")
    private String insidePicture;

    @ApiModelProperty(value = "收银台照")
    private String cashRegisterPicture;

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
