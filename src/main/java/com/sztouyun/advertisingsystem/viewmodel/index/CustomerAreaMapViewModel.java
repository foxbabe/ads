package com.sztouyun.advertisingsystem.viewmodel.index;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * Created by wenfeng on 2017/9/22.
 */
@ApiModel
public class CustomerAreaMapViewModel {
    @ApiModelProperty(value = "广告客户总数")
    private Long totalAmount;

    @ApiModelProperty(value = "区域最小客户数")
    private Integer minAmount;

    @ApiModelProperty(value = "区域最大客户数")
    private Integer maxAmount;

    @ApiModelProperty(value = "区域客户列表")
    private List<CustomerAreaStatistic> list;

    public Long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(Integer minAmount) {
        this.minAmount = minAmount;
    }

    public Integer getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(Integer maxAmount) {
        this.maxAmount = maxAmount;
    }

    public List<CustomerAreaStatistic> getList() {
        return list;
    }

    public void setList(List<CustomerAreaStatistic> list) {
        this.list = list;
    }
}
