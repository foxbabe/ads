package com.sztouyun.advertisingsystem.model.system;

import com.sztouyun.advertisingsystem.model.BaseModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Date;

/**
 * Created by wenfeng on 2017/8/29.
 */
@Entity
public class AdvertisementDisplayTimesConfig extends BaseModel {

    /**
     * 时间单位
     */
    @Column(nullable = false)
    private Integer timeUnit=1;

    /**
     * 展示次数
     */
    @Column(nullable = false)
    private Integer  displayTimes;


    @Column(nullable = false)
    private Date updatedTime;

    public Integer getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(Integer timeUnit) {
        this.timeUnit = timeUnit;
    }

    public Integer getDisplayTimes() {
        return displayTimes;
    }

    public void setDisplayTimes(Integer displayTimes) {
        this.displayTimes = displayTimes;
    }

    @Override
    public Date getUpdatedTime() {
        return updatedTime;
    }

    @Override
    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }
}
