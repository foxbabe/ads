package com.sztouyun.advertisingsystem.viewmodel.storeDevice;

import com.sztouyun.advertisingsystem.viewmodel.DayRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by szty on 2018/8/9.
 */
@Data
@ApiModel
public class OpeningTimeTrendRequest extends DayRequest {
    @ApiModelProperty(value = "时间间隔")
    private Integer interval;
}
