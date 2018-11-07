package com.sztouyun.advertisingsystem.viewmodel.commodity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CommodityTypeOptionViewModel extends SupplierOptionViewModel{

    @ApiModelProperty(value = "商品分类Id")
    private Long commodityTypeId;

    @ApiModelProperty(value = "商品分类名称")
    private String commodityTypeName;

}
