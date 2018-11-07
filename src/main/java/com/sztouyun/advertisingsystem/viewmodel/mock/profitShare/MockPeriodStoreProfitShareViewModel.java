package com.sztouyun.advertisingsystem.viewmodel.mock.profitShare;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel
public class MockPeriodStoreProfitShareViewModel {

    @ApiModelProperty(value = "年份")
    private Integer year;

    @ApiModelProperty(value = "月份集合")
    private List<Integer> mongth;

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public List<Integer> getMongth() {
        return mongth;
    }

    public void setMongth(List<Integer> mongth) {
        this.mongth = mongth;
    }
}
