package com.sztouyun.advertisingsystem.viewmodel.index;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by wenfeng on 2017/9/20.
 */

@ApiModel
public class DistributionStatisticDto<T>{
    @ApiModelProperty(value = "枚举类型")
    private T keyValue;

    private Long value;

    public T getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(T keyValue) {
        this.keyValue = keyValue;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }
}
