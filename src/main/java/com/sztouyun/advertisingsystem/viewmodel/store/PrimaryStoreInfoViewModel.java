package com.sztouyun.advertisingsystem.viewmodel.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by wenfeng on 2018/4/19.
 */
@ApiModel
@Data
public class PrimaryStoreInfoViewModel {

    @ApiModelProperty(value = "门店ID")
    private String id;

    @ApiModelProperty(value = "门店名称")
    private String storeName;

    @ApiModelProperty(value = "门店地址")
    private String storeAddress;

    @ApiModelProperty(value = "门头照")
    private String outsidePicture;

    @ApiModelProperty(value = "店内照")
    private String insidePicture;

    @ApiModelProperty(value = "收银台照")
    private String cashRegisterPicture;

    @ApiModelProperty(value = "是否选中")
    private boolean checked;
}
