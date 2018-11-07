package com.sztouyun.advertisingsystem.viewmodel.job;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sztouyun.advertisingsystem.common.Constant;
import lombok.Data;

import java.util.Date;

/**
 * Created by wenfeng on 2018/3/22.
 */
@Data
public class StoreOpeningTime {
    @JsonProperty("shopId")
    private String shopId;
    @JsonProperty("beginTime")
    @JsonFormat(pattern = Constant.DATETIME, timezone = "GMT+8")
    private Date beginTime;
    @JsonProperty("endTime")
    @JsonFormat(pattern = Constant.DATETIME, timezone = "GMT+8")
    private Date endTime;
}
