package com.sztouyun.advertisingsystem.viewmodel.adProfitShare;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * Created by wenfeng on 2018/1/23.
 */
@ApiModel
public class SubPeriodStoreProfitListRequest {
    @ApiModelProperty(value = "门店ID",required = true)
    private String storeId;
    @ApiModelProperty(value = "选中行的结算月份",required = true)
    @JsonFormat(pattern = Constant.DATA_YM, timezone = "GMT+8")
    private Date settledMonth;

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public Date getSettledMonth() {
        return settledMonth;
    }

    public void setSettledMonth(Date settledMonth) {
        this.settledMonth = settledMonth;
    }
}
