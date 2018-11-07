package com.sztouyun.advertisingsystem.viewmodel.customer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class CustomerRankingIndustryViewModel {

    @ApiModelProperty(value = "行业名称")
    private String industry;

    @ApiModelProperty(value = "行业Id")
    private String industryId;

    @ApiModelProperty(value = "客户数量")
    private Long customerCount;

    @ApiModelProperty(value = "客户占比")
    private String customerRatio ;
}
