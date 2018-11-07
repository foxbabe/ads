package com.sztouyun.advertisingsystem.viewmodel.advertisement;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class AutoFinishAdvertisementInfo {
    private Integer advertisementStatus;

    private Date date;

    public AutoFinishAdvertisementInfo(Integer advertisementStatus, Date date) {
        this.advertisementStatus = advertisementStatus;
        this.date = date;
    }
}
