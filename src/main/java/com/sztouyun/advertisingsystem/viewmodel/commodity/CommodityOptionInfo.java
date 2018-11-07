package com.sztouyun.advertisingsystem.viewmodel.commodity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CommodityOptionInfo{

    @ApiModelProperty(value = "供应商Id")
    private Long supplierId;

    @ApiModelProperty(value = "商品分类Id")
    private Long commodityTypeId;

}
