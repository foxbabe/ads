package com.sztouyun.advertisingsystem.viewmodel.partner.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel
public class StoreProcessRequest {

    @ApiModelProperty(name = "广告门店Id")
    private String partnerAdvertisementStoreId;

    @ApiModelProperty(name = "创建时间")
    private Date createdTime;

}
