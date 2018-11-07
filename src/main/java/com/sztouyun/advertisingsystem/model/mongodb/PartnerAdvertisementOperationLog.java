package com.sztouyun.advertisingsystem.model.mongodb;

import lombok.Data;

import java.util.Date;

@Data
public class PartnerAdvertisementOperationLog {
    public PartnerAdvertisementOperationLog() {
    }

    public PartnerAdvertisementOperationLog(String partnerAdvertisementId, Integer advertisementStatus) {
        this.partnerAdvertisementId = partnerAdvertisementId;
        this.advertisementStatus = advertisementStatus;
    }

    public PartnerAdvertisementOperationLog(String partnerAdvertisementId, Integer advertisementStatus, String operator, String remark) {
        this.partnerAdvertisementId = partnerAdvertisementId;
        this.advertisementStatus = advertisementStatus;
        this.operator = operator;
        this.remark = remark;
    }

    /**
     * 合作方广告ID （合作方类型+thirdPartId）
     */
    private String partnerAdvertisementId;

    /**
     * 合作方广告状态
     */
    private Integer advertisementStatus;

    /**
     * 操作人
     */
    private String operator;

    /**
     * 备注
     */
    private String remark;

    private Long createdTime =new Date().getTime();
}
