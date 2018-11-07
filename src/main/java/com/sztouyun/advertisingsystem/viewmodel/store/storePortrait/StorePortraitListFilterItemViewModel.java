package com.sztouyun.advertisingsystem.viewmodel.store.storePortrait;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@ApiModel
public class StorePortraitListFilterItemViewModel {

    @ApiModelProperty(value = "门店画像的类型")
    private Integer filterItemValue;

    @ApiModelProperty(value = "门店画像的名称")
    private String filterItemName;

    @ApiModelProperty(value = "字段名")
    private String fieldName;

    @ApiModelProperty(value = "门店画像的信息")
    private Map<Integer,String> filterItemMap = new HashMap<>();

    public StorePortraitListFilterItemViewModel(Integer filterItemValue, String filterItemName, String fieldName) {
        this.filterItemValue = filterItemValue;
        this.filterItemName = filterItemName;
        this.fieldName = fieldName;
    }

    public StorePortraitListFilterItemViewModel() {
    }
}
