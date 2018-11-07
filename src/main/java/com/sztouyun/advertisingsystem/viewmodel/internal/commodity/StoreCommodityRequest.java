package com.sztouyun.advertisingsystem.viewmodel.internal.commodity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Created by wenfeng on 2018/4/19.
 */
@ApiModel
@Data
public class StoreCommodityRequest extends BaseCommodityRequest{
    @ApiModelProperty(value = "门店编号",required =true)
    @NotNull(message = "门店编号不允许为空")
    private String storeNo;

    @ApiModelProperty(value = "商品ID",required = true)
    private Long commodityId;
}
