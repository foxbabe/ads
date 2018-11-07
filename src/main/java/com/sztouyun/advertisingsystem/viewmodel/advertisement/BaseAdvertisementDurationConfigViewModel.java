package com.sztouyun.advertisingsystem.viewmodel.advertisement;

import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@ApiModel
public class BaseAdvertisementDurationConfigViewModel {


    @ApiModelProperty(value = "时长", required = true)
    @NotNull(message = "请输入有效的时长")
    @Min(value = 0, message = "时长必须大于0")
    @Max(value= Constant.INTEGER_MAX,message = "时间周期最大值不能超过999999999")
    private Integer duration;

    @ApiModelProperty(value = "时长单位: 1:时 2:分 3:秒", required = true)
    @NotNull(message = "时长单位不能为空")
    private Integer durationUnit;


    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getDurationUnit() {
        return durationUnit;
    }

    public void setDurationUnit(Integer durationUnit) {
        this.durationUnit = durationUnit;
    }
}
