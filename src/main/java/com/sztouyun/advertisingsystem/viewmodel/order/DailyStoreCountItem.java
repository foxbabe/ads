package com.sztouyun.advertisingsystem.viewmodel.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * Created by wenfeng on 2018/2/5.
 */
@Data
@ApiModel
public class DailyStoreCountItem {
    @ApiModelProperty(value = "投放时间")
    @JsonFormat(pattern = Constant.DATA_YMD_CN)
    private Date date;
    @ApiModelProperty(value = "投放门店数")
    private Long storeCount;
}
