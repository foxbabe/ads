package com.sztouyun.advertisingsystem.viewmodel.internal.commodity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * Created by wenfeng on 2018/4/19.
 */
@ApiModel
@Data
public class CommodityRequest extends BaseCommodityRequest{
    @ApiModelProperty(value = "商品code",required =true)
    @NotEmpty(message = "商品CODE不允许为空")
    private String code;

    @ApiModelProperty(value = "供应商ID",required = true)
    @NotNull(message = "供应商ID不允许为空")
    private Long supplierId;

    @ApiModelProperty(value = "商品类别ID",required = true)
    @NotNull(message = "商品类别不允许为空")
    private Long commodityTypeId;
}
