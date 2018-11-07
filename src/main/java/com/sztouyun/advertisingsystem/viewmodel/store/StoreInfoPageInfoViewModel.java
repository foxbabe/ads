package com.sztouyun.advertisingsystem.viewmodel.store;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.annotation.validation.EnumValue;
import com.sztouyun.advertisingsystem.model.store.StoreSourceEnum;
import com.sztouyun.advertisingsystem.viewmodel.commodity.CommodityOptionViewModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ApiModel
@Data
public class StoreInfoPageInfoViewModel extends BaseStoreInfoPageInfoViewModel {

    @ApiModelProperty(value = "合同Id")
    private String contractId;

    @ApiModelProperty(value = "门店类型")
    private Integer storeType;

    @ApiModelProperty(value ="门店的来源,全部:null, 1:运维门店 2:运维新门店")
    @EnumValue(enumClass = StoreSourceEnum.class,message = "门店来源不匹配",nullable = true)
    private Integer storeSource;

    @ApiModelProperty(value = "心跳状态 有心跳: true;  没心跳: false; 全部: 不传这个值")
    private Boolean hasHeart;

    @ApiModelProperty(value = "心跳开始时间")
    @JsonFormat(pattern = Constant.DATA_YMD, timezone = "GMT+8")
    private Date heartStartTime;

    @ApiModelProperty(value = "心跳结束时间")
    @JsonFormat(pattern = Constant.DATA_YMD, timezone = "GMT+8")
    private Date heartEndTime;

    @ApiModelProperty(value = "是否使用坐标搜索", required = true)
    private Boolean isWithCoordinate = Boolean.FALSE;

    @ApiModelProperty(value = "是否铺货")
    private Boolean isPaveGoods;

    @ApiModelProperty(value = "铺货信息列表")
    private List<CommodityOptionViewModel> paveGoodsList = new ArrayList<>();

    @ApiModelProperty(value = "是否选中")
    private Boolean isCheck;

}
