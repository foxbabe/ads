package com.sztouyun.advertisingsystem.service.profitshare.operations.data;

import com.sztouyun.advertisingsystem.service.rule.base.data.BaseRuleData;

import java.util.Date;


public class ProfitShareRuleData<TRuleInfo>  extends BaseRuleData<TRuleInfo> {
    private Date dateTime;

    public ProfitShareRuleData(String objectId,Date dateTime) {
        super(objectId);
        this.dateTime = dateTime;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }
}
