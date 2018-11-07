package com.sztouyun.advertisingsystem.viewmodel.store;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.annotation.validation.EnumValue;
import com.sztouyun.advertisingsystem.model.store.StoreSourceEnum;
import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;

@ApiModel
@Data
public class StoreOrderRequest extends BasePageInfo {

    @ApiModelProperty(value = "订单ID)")
    @NotBlank(message = "订单ID不能为空")
    private String orderId;

    @ApiModelProperty(value ="门店的来源,全部:null, 1:运维门店 2:运维新门店")
    @EnumValue(enumClass = StoreSourceEnum.class,message = "门店来源不匹配",nullable = true)
    private Integer storeSource;

    @ApiModelProperty(value = "门店名称")
    private String storeName;

    @ApiModelProperty(value = "设备ID")
    private String deviceId;

    @ApiModelProperty(value = "门店ID")
    private String shopId;

    @ApiModelProperty(value="开始时间")
    @JsonFormat(pattern = Constant.DATA_YMD, timezone = "GMT+8")
    private Date startTime;
    @ApiModelProperty(value="结束时间")
    @JsonFormat(pattern = Constant.DATA_YMD, timezone = "GMT+8")
    private Date endTime;

    @ApiModelProperty(value = "门店是否可用")
    private Boolean available;

}
