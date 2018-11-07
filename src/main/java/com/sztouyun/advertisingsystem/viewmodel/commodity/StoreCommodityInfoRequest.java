package com.sztouyun.advertisingsystem.viewmodel.commodity;

import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class StoreCommodityInfoRequest extends BasePageInfo {

    @ApiModelProperty(value = "门店Id",required = true)
    @NotBlank(message = "门店Id不能为空")
    private String storeId;

    @ApiModelProperty(value = "商品名称")
    private String commodityName;

    @ApiModelProperty(value = "供应商Id")
    private Long supplierId;

    @ApiModelProperty(value = "商品分类Id")
    private Long commodityTypeId;

}
