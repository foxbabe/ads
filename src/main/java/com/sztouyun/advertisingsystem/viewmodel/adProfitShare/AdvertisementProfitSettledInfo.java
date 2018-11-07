package com.sztouyun.advertisingsystem.viewmodel.adProfitShare;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdvertisementProfitSettledInfo {
    private Date startDate;

    private Date endDate;

    private String id;
}
