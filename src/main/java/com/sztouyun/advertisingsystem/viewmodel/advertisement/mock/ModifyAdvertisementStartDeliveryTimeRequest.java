package com.sztouyun.advertisingsystem.viewmodel.advertisement.mock;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.Date;

@ApiModel
public class ModifyAdvertisementStartDeliveryTimeRequest {
    @ApiModelProperty(value = "广告ID)",required = true)
    @NotBlank(message = "广告ID不能为空")
    private String advertisementId;

    @JsonFormat(pattern = Constant.DATETIME, timezone = "GMT+8")
    @ApiModelProperty(value = "开始投放时间)",required = true)
    @NotNull(message = "开始投放时间不能为空")
    private Date startDeliveryTime;

    public String getAdvertisementId() {
        return advertisementId;
    }

    public void setAdvertisementId(String advertisementId) {
        this.advertisementId = advertisementId;
    }

    public Date getStartDeliveryTime() {
        return startDeliveryTime;
    }

    public void setStartDeliveryTime(Date startDeliveryTime) {
        this.startDeliveryTime = startDeliveryTime;
    }
}
