package com.sztouyun.advertisingsystem.viewmodel.adProfitShare;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@ApiModel
public class SettledMessageNoticeViewModel {
    @ApiModelProperty(value = "是否提醒(true:提醒,false:不提醒)")
    private boolean remind;

    @ApiModelProperty(value = "提醒日期")
    @JsonFormat(pattern = Constant.DATA_YM_CN, timezone = "GMT+8")
    private Date remindDate;

    public boolean isRemind() {
        return remind;
    }

    public void setRemind(boolean remind) {
        this.remind = remind;
    }

    public Date getRemindDate() {
        return remindDate;
    }

    public void setRemindDate(Date remindDate) {
        this.remindDate = remindDate;
    }
}
