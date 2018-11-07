package com.sztouyun.advertisingsystem.viewmodel.commodity;

import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class CommodityOptionRequest extends BasePageInfo {

    @ApiModelProperty(value = "商品名称")
    private String commodityName;

    @ApiModelProperty(value = "供应商Id和商品分类Id")
    private List<CommodityOptionInfo> commodityOptionInfos;

}
