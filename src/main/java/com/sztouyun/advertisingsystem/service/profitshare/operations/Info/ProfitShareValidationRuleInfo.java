package com.sztouyun.advertisingsystem.service.profitshare.operations.Info;

import com.sztouyun.advertisingsystem.service.rule.base.data.ValidationRuleInfo;

import java.util.Date;

public class ProfitShareValidationRuleInfo extends ValidationRuleInfo {
    private Date dateTime;

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }
}
