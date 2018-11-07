package com.sztouyun.advertisingsystem.viewmodel.adProfitShare;

import com.sztouyun.advertisingsystem.utils.EnumUtils;
import com.sztouyun.advertisingsystem.viewmodel.customerStore.StoreInvalidTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by szty on 2018/8/23.
 */
@ApiModel
@Data
public class InvalidEffectProfitStoreInfo {
    @ApiModelProperty(value = "门店ID")
    private String shopId;
    @ApiModelProperty(value = "门店名称")
    private String storeName;
    @ApiModelProperty(value = "单价D")
    private Integer unitPrice;
    @ApiModelProperty(value = "数量")
    private Integer count;
    @ApiModelProperty(value = "分成金额")
    private Long shareAmount;
    @ApiModelProperty(value = "无效类型")
    private Integer validType;

    public String getRemark(){
        return EnumUtils.toEnum(validType, StoreInvalidTypeEnum.class).getDisplayName();
    }

}
