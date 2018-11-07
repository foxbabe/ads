package com.sztouyun.advertisingsystem.viewmodel.advertisement;


import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created by wenfeng on 2017/8/4.
 */
@ApiModel
public class BaseAdvertisementDisplayTimesConfigViewModel {
    @ApiModelProperty(value = "时间单位[1每天，2每周]", required = true)
    @NotNull(message = "时间单位不能为空")
    @Min(value = 1, message = "时间单位不存在")
    @Max(value = 2, message = "时间单位不存在")
    private Integer timeUnit;

    @ApiModelProperty(value = "展示次数", required = true)
    @Max(value = Constant.INTEGER_MAX, message = "展示次数最大值不能超过999999999")
    @NotNull(message = "展示次数不能为空")
    @Min(value = 1, message = "展示次数不存在")
    private Integer displayTimes;

    public Integer getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(Integer timeUnit) {
        this.timeUnit = timeUnit;
    }

    public Integer getDisplayTimes() {
        return displayTimes;
    }

    public void setDisplayTimes(Integer displayTimes) {
        this.displayTimes = displayTimes;
    }
}



