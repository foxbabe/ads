package com.sztouyun.advertisingsystem.model.system;

import com.sztouyun.advertisingsystem.model.BaseModel;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class AdvertisementDurationConfig extends BaseModel {

    /**
     * 时长
     */
    @Column(nullable = false)
    private Integer duration;

    /**
     * 时长单位
     */
    @Column(nullable = false)
    private Integer durationUnit;

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getDurationUnit() {
        return durationUnit;
    }

    public void setDurationUnit(Integer durationUnit) {
        this.durationUnit = durationUnit;
    }
}
