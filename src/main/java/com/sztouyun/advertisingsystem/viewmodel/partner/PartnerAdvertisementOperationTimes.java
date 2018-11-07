package com.sztouyun.advertisingsystem.viewmodel.partner;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by szty on 2018/7/5.
 */
@Data
public class PartnerAdvertisementOperationTimes {
    @ApiModelProperty(value = "请求次数")
    private Long requestTimes;

    @ApiModelProperty(value = "展示次数")
    private Long displayTimes;

    @ApiModelProperty(value = "有效次数")
    private Long validDisplayTimes;
}
