package com.sztouyun.advertisingsystem.common;

public class TimeSpan {
    private Long startTime = 0L;

    private Long endTime= 0L;

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public TimeSpan(Long startTime, Long endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public TimeSpan(){

    }
}
