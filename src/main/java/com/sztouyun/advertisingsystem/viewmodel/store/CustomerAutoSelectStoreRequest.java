package com.sztouyun.advertisingsystem.viewmodel.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created by szty on 2018/5/17.
 */
@ApiModel
@Data
public class CustomerAutoSelectStoreRequest extends CustomerStoreInfoPageInfoRequest {
    @ApiModelProperty(value = "自动选择记录数",required = true)
    @Min(value = 1,message = "选取门店数不能少于1")
    private Integer recordCount;
}
