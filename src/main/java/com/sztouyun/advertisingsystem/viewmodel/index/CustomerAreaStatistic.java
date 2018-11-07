package com.sztouyun.advertisingsystem.viewmodel.index;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by wenfeng on 2017/9/22.
 */
@ApiModel
public class CustomerAreaStatistic {
    @ApiModelProperty(value = "区域名称")
    private String areaName;

    @ApiModelProperty(value = "客户数量")
    private Integer customerCount;

    @ApiModelProperty(value = "客户占比")
    private String customerRatio ;

    @ApiModelProperty(value = "签约率")
    private String signRatio ;

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public Integer getCustomerCount() {
        return customerCount;
    }

    public void setCustomerCount(Integer customerCount) {
        this.customerCount = customerCount;
    }

    public String getCustomerRatio() {
        return customerRatio;
    }

    public void setCustomerRatio(String customerRatio) {
        this.customerRatio = customerRatio;
    }

    public String getSignRatio() {
        return signRatio;
    }

    public void setSignRatio(String signRatio) {
        this.signRatio = signRatio;
    }
}
