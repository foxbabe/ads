package com.sztouyun.advertisingsystem.viewmodel.adProfitShare;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by wenfeng on 2018/1/12.
 */
@Data
@ApiModel
public class StoreProfitItem extends BaseStoreProfit{
    @ApiModelProperty(value = "省")
    private  String provinceName;

    @ApiModelProperty(value = "市")
    private  String cityName;

    @ApiModelProperty(value = "区")
    private  String regionName;

    @ApiModelProperty(value = "可否查看")
    private Boolean canView;
}
