package com.sztouyun.advertisingsystem.viewmodel.partner;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by szty on 2018/5/10.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartnerAdvertisementInfo {
    private String id;
    private String storeId;
    private Integer advertisementPositionType;
}
