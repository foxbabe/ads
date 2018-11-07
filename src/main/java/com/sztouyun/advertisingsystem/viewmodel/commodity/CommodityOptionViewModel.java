package com.sztouyun.advertisingsystem.viewmodel.commodity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CommodityOptionViewModel extends CommodityTypeOptionViewModel{

    @ApiModelProperty(value = "商品Id")
    private Long commodityId;

    @ApiModelProperty(value = "商品名称")
    private String commodityName;

}
