package com.sztouyun.advertisingsystem.viewmodel.store;

import com.sztouyun.advertisingsystem.common.annotation.validation.EnumValue;
import com.sztouyun.advertisingsystem.model.contract.StoreTypeEnum;
import com.sztouyun.advertisingsystem.viewmodel.commodity.CommodityOptionViewModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ApiModel
@NoArgsConstructor
@Data
public class StoreInfoAreaSelectedViewModel extends StoreInfoAreaSelectedRequest{
    @ApiModelProperty(value = "合同id", required = true)
    private String contractId;

    @ApiModelProperty(value = "门店类型", required = true)
    @NotNull(message = "门店类型不能为空")
    @EnumValue(enumClass = StoreTypeEnum.class,message = "门店类型不匹配")
    private Integer storeType;

    private Boolean hasHeart;

    private Date heartStartTime;

    private Date heartEndTime;

    @ApiModelProperty(value = "是否为门店画像列表")
    private boolean isStorePortrait;

    @ApiModelProperty(value = "是否铺货")
    private Boolean isPaveGoods;

    @ApiModelProperty(value = "铺货信息列表")
    private List<CommodityOptionViewModel> paveGoodsList = new ArrayList<>();


    @ApiModelProperty(hidden = true)
    private PaveGoodsConditionInfo paveGoodsConditionInfo = new PaveGoodsConditionInfo();

    public StoreInfoAreaSelectedViewModel(String contractId) {
        this.contractId = contractId;
    }
    public StoreInfoAreaSelectedViewModel(boolean isStorePortrait) {
        this.isStorePortrait=isStorePortrait;
    }

}
