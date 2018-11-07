package com.sztouyun.advertisingsystem.viewmodel.adProfitShare;

public class AdvertisementStoreProfitPeriodStatisticInfo {


    /**
     * 广告ID
     */
    private String advertisementId;
    /**
     * 门店下该广告总的分成
     */
    private Double shareAmount;

    public String getAdvertisementId() {
        return advertisementId;
    }

    public void setAdvertisementId(String advertisementId) {
        this.advertisementId = advertisementId;
    }

    public Double getShareAmount() {
        return shareAmount;
    }

    public void setShareAmount(Double shareAmount) {
        this.shareAmount = shareAmount;
    }
}
