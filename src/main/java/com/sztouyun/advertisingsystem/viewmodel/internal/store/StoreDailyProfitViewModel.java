package com.sztouyun.advertisingsystem.viewmodel.internal.store;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel
public class StoreDailyProfitViewModel {

    @ApiModelProperty(value = "日期")
    @JsonFormat(pattern = Constant.DATA_YMD_CN, timezone = "GMT+8")
    private Date dateTime ;

    @ApiModelProperty(value="分成金额(单位:分)")
    private Long shareAmount;

    @JsonIgnore
    private Long date;

    public Date getDateTime() {
        return new Date(date);
    }
}
