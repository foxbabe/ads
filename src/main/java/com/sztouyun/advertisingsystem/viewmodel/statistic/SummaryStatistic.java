package com.sztouyun.advertisingsystem.viewmodel.statistic;

import com.sztouyun.advertisingsystem.utils.EnumUtils;

import java.util.Date;

public class SummaryStatistic {

    private Long total;

    private Integer increment;

    private Integer oneWeekAgoTotal;

    private Integer statisticType;

    private Date startDate;

    private Date endDate;

    private Integer totalBeforeZeroTime;


    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Integer getTotalBeforeZeroTime() {
        return totalBeforeZeroTime;
    }

    public void setTotalBeforeZeroTime(Integer totalBeforeZeroTime) {
        this.totalBeforeZeroTime = totalBeforeZeroTime;
    }

    public Integer getIncrement() {
        return increment;
    }

    public void setIncrement(Integer increment) {
        this.increment = increment;
    }

    public Integer getOneWeekAgoTotal() {
        return oneWeekAgoTotal;
    }

    public void setOneWeekAgoTotal(Integer oneWeekAgoTotal) {
        this.oneWeekAgoTotal = oneWeekAgoTotal;
    }

    public Integer getStatisticType() {
        return statisticType;
    }

    public void setStatisticType(Integer statisticType) {
        this.statisticType = statisticType;
    }

    public SummaryStatisticTypeEnum getStatisticsTypeEnum() {
        return EnumUtils.toEnum(statisticType, SummaryStatisticTypeEnum.class);
    }
}
