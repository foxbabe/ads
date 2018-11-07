package com.sztouyun.advertisingsystem.viewmodel.store;

import com.sztouyun.advertisingsystem.common.annotation.validation.EnumValue;
import com.sztouyun.advertisingsystem.model.store.StoreSourceEnum;
import com.sztouyun.advertisingsystem.viewmodel.AreaRequest;
import com.sztouyun.advertisingsystem.viewmodel.commodity.CommodityOptionViewModel;
import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@ApiModel
public class StoreInfoStatisticQueryRequest extends AreaRequest {
    @ApiModelProperty(value = "全部:0, 1:已激活 2:未激活 3:未使用")
    private int status;

    @ApiModelProperty(value = "门店名称")
    private String storeName;

    @ApiModelProperty(value = "设备ID")
    private String deviceId;

    @ApiModelProperty(value = "门店ID")
    private String shopId;

    @ApiModelProperty(value = "门店是否可用")
    private Boolean available;

    @ApiModelProperty(value ="门店的来源,全部:null, 1:运维门店 2:运维新门店")
    @EnumValue(enumClass = StoreSourceEnum.class,message = "门店来源不匹配",nullable = true)
    private Integer storeSource;

    @ApiModelProperty(value="是否分成; 全部：null,分成:true, 不分成:false")
    private Boolean enableProfitShare;

    @ApiModelProperty(value = "是否铺货")
    private Boolean isPaveGoods;

    @ApiModelProperty(value="有无门店画像")
    private Boolean isStorePortrait;

    @ApiModelProperty(value = "是否达标")
    private Boolean isQualified;

    @ApiModelProperty(value = "铺货信息列表")
    private List<CommodityOptionViewModel> paveGoodsList = new ArrayList<>();

    @ApiModelProperty(hidden = true)
    private PaveGoodsConditionInfo paveGoodsConditionInfo = new PaveGoodsConditionInfo();

    public Boolean getActive() {
        if(getStatus() == 0 || getStatus() == 3)
            return null;
        return getStatus() == 1;
    }

    public Boolean getUsed() {
        if(getStatus() == 0)
            return null;
        return getStatus() != 3;
    }
}
