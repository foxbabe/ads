package com.sztouyun.advertisingsystem.viewmodel.partner;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class PartnerAdvertisementStoreInfo implements Serializable{

    private String partnerAdvertisementStoreId;

    private String partnerAdvertisementId;

    private String storeId;

    private Integer advertisementPositionType;

    private String monitorUrls;

    private Date startTime;

    private Date endTime;

    private String partnerId;

    private String thirdPartId;

    private Integer duration;

    private String requestId;

    private List<PartnerAdvertisementMaterialInfo> partnerAdvertisementMaterialInfoList = new ArrayList<>();
}
