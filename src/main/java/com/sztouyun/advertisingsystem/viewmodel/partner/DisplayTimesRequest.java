package com.sztouyun.advertisingsystem.viewmodel.partner;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.annotation.validation.EnumValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel
public class DisplayTimesRequest {
    @ApiModelProperty("开始日期")
    @JsonFormat(pattern = Constant.DATA_YMD)
    private Date beginDate;

    @ApiModelProperty("结束日期")
    @JsonFormat(pattern = Constant.DATA_YMD)
    private Date endDate;

    @ApiModelProperty("排序: 1 - 展示次数, 2 - 有效次数")
    @EnumValue(enumClass = PartnerDisplayTimesSortEnum.class, message = "排序枚举值不正确")
    private Integer sortNumber;
}
