package com.sztouyun.advertisingsystem.viewmodel.store;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StorePlacementViewModel {

    @ApiModelProperty(value = "门店ID")
    private String id;

    @ApiModelProperty(value = "经度")
    private Double longitude;

    @ApiModelProperty(value = "维度")
    private Double latitude;
}
