package com.sztouyun.advertisingsystem.model.system;

import com.sztouyun.advertisingsystem.model.BaseRecordModel;

import javax.persistence.Entity;

@Entity
public class AdvertisementDeliveryStrategy extends BaseRecordModel {

    /**
     * 是否强制每天展示次数投放
     */
    private Boolean controlDisplayTimes;

    public Boolean getControlDisplayTimes() {
        return controlDisplayTimes;
    }

    public void setControlDisplayTimes(Boolean controlDisplayTimes) {
        this.controlDisplayTimes = controlDisplayTimes;
    }
}
