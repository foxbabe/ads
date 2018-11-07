package com.sztouyun.advertisingsystem.viewmodel.index;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by wenfeng on 2017/9/20.
 */

@ApiModel
public class DistributionStatistic<T> implements  Comparable<DistributionStatistic<T>>{
    @ApiModelProperty(value = "keyValue")
    private Integer keyValue;

    @ApiModelProperty(value = "名称（客户是省市名称，其他事类型名称）")
    private T name;

    @ApiModelProperty(value = "数量")
    private Long value;

    @ApiModelProperty(value = "比例")
    private String ratio;

    public Integer getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(Integer keyValue) {
        this.keyValue = keyValue;
    }

    public T getName() {
        return name;
    }

    public void setName(T name) {
        this.name = name;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public String getRatio() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }

    /**
     * 升序
     * @param o
     * @return
     */
    @Override
    public int compareTo(DistributionStatistic o) {
        if(this.keyValue>o.getKeyValue())
            return 1;
        if(this.keyValue<o.getKeyValue())
            return -1;
        return 0;
    }
}
