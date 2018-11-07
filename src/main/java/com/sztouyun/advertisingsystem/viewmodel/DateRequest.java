package com.sztouyun.advertisingsystem.viewmodel;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * Created by wenfeng on 2018/1/12.
 */
@ApiModel
@Data
public class DateRequest extends BasePageInfo {
    @ApiModelProperty(value = "开始时间")
    @JsonFormat(pattern = Constant.DATETIME, timezone = "GMT+8")
    private Date startDate;

    @ApiModelProperty(value = "结束时间")
    @JsonFormat(pattern = Constant.DATETIME, timezone = "GMT+8")
    private Date endDate;
}
