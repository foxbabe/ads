package com.sztouyun.advertisingsystem.viewmodel.adProfitShare;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;

/**
 * 收益流水(入参)
 * @author guangpu.yan
 * @create 2018-01-11 10:04
 **/
@Data
@ApiModel
public class StoreProfitStreamRequest extends BasePageInfo {
    @ApiModelProperty(value = "开机时长是否达标")
    private Boolean openingTimeStandardIs;

    @ApiModelProperty(value = "订单数量是否达标")
    private Boolean orderStandardIs;

    @ApiModelProperty(value = "开始时间")
    @JsonFormat(pattern = Constant.DATA_YMD, timezone = "GMT+8")
    private Date beginTime;

    @ApiModelProperty(value = "结束时间")
    @JsonFormat(pattern = Constant.DATA_YMD, timezone = "GMT+8")
    private Date endTime;

    @NotBlank(message = "门店id不能为空")
    @ApiModelProperty(value = "门店id")
    private String storeId;

    @ApiModelProperty(value = "是否结算")
    private Boolean settled;
}
