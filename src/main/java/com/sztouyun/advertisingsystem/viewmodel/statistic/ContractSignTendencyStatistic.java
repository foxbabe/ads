package com.sztouyun.advertisingsystem.viewmodel.statistic;

import java.util.Date;

public class ContractSignTendencyStatistic {

    private Date signTime;

    private Long total;

    public Date getSignTime() {
        return signTime;
    }

    public void setSignTime(Date signTime) {
        this.signTime = signTime;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
