package com.sztouyun.advertisingsystem.viewmodel.store.openapi;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.annotation.validation.EnumValue;
import com.sztouyun.advertisingsystem.model.system.AdvertisementPositionCategoryEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;


@Data
@ApiModel
public class QueryUnavailableStoreRequest {
    @ApiModelProperty(value = "广告位种类", required = true)
    @EnumValue(enumClass = AdvertisementPositionCategoryEnum.class,message = "广告位种类不匹配")
    private Integer advertisementPositionCategory;

    @ApiModelProperty(value = "设备id列表")
    private List<String> storeIds;

    @ApiModelProperty(value = "开始时间")
    @JsonFormat(pattern = Constant.DATA_YMD, timezone = "GMT+8")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    @JsonFormat(pattern = Constant.DATA_YMD, timezone = "GMT+8")
    private Date entTime;
}
