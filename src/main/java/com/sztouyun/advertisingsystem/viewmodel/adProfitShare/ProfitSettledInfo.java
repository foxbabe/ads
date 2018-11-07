package com.sztouyun.advertisingsystem.viewmodel.adProfitShare;

/**
 * Created by wenfeng on 2018/1/26.
 */
public class ProfitSettledInfo {
    private Integer year;
    private Integer month;
    private Double settledAmount;
    private Double unsettledAmount;

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Double getSettledAmount() {
        return settledAmount;
    }

    public void setSettledAmount(Double settledAmount) {
        this.settledAmount = settledAmount;
    }

    public Double getUnsettledAmount() {
        return unsettledAmount;
    }

    public void setUnsettledAmount(Double unsettledAmount) {
        this.unsettledAmount = unsettledAmount;
    }

    public Double getShareAmount(){
        return settledAmount+unsettledAmount;
    }
}
