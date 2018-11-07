package com.sztouyun.advertisingsystem.viewmodel.commodity;

import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class CommodityTypeOptionRequest extends BasePageInfo {

    @ApiModelProperty(value = "供应商Id")
    private String supplierId;

    @ApiModelProperty(value = "商品分类名称")
    private String commodityTypeName;

    public List<Integer> getSupplierIds() {
        return com.sztouyun.advertisingsystem.utils.StringUtils.stringToInts(supplierId, Constant.SEPARATOR);
    }

}
