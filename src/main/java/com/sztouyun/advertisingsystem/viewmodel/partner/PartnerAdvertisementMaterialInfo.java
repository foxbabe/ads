package com.sztouyun.advertisingsystem.viewmodel.partner;

import lombok.Data;

@Data
public class PartnerAdvertisementMaterialInfo {
    private Integer materialType;

    private String materialSpecification = "";

    private String materialSize = "";

    private String originalUrl;

    private String url;

    private String md5;

    private Integer duration;
}
