package com.sztouyun.advertisingsystem.viewmodel.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.viewmodel.advertisement.TerminalAdvertisementConfigInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Created by wenfeng on 2018/4/3.
 */
@Data
@ApiModel
public class AdvertisementStoreTaskViewModel extends TaskViewModel {
    @ApiModelProperty(value = "门店编号")
    private String storeNo;
    @ApiModelProperty(value = "门店名称")
    private String storeName;
    @ApiModelProperty(value = "广告名称")
    private String advertisementName;
    @ApiModelProperty(value = "合同名称")
    private String contractName;
    @ApiModelProperty(value = "合同编号")
    private String contractCode;
    @ApiModelProperty(value = "是否新门店")
    private Boolean newStore;
    @ApiModelProperty(value = "素材列表", required = false)
    private List<TerminalAdvertisementConfigInfo> materialItems;
}
