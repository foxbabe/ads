package com.sztouyun.advertisingsystem.model.adProfitShare;

import com.sztouyun.advertisingsystem.model.BaseModel;
import com.sztouyun.advertisingsystem.model.advertisement.Advertisement;
import com.sztouyun.advertisingsystem.model.contract.Contract;
import com.sztouyun.advertisingsystem.model.store.StoreInfo;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(indexes = {
        @Index(name = "index_store_profit_statistic_id",columnList = "store_profit_statistic_id"),
        @Index(name = "index_store_id_advertisement_id",columnList = "store_id,advertisement_id")
})
public class AdvertisementProfitStatistic extends BaseModel {

    /**
     * 门店收益统计表ID
     */
    @Column(name = "store_profit_statistic_id",nullable = false,updatable = false,length = 36)
    private String storeProfitStatisticId;

    /**
     * 门店ID
     */
    @Column(name = "store_id",nullable = false,updatable = false,length = 36)
    private String storeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id",insertable = false,updatable = false)
    private StoreInfo storeInfo;

    /**
     * 合同ID
     */
    @Column(name = "contract_id",nullable = false,updatable = false,length = 36)
    private String contractId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id",insertable = false,updatable = false)
    private Contract contract;

    /**
     * 广告ID
     */
    @Column(name = "advertisement_id",nullable = false,updatable = false,length = 36)
    private String advertisementId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "advertisement_id",insertable = false,updatable = false)
    private Advertisement advertisement;

    /**
     * 是否激活
     */
    @Column(nullable = false)
    private boolean actived;

    /**
     * 是否开启分成
     */
    @Column(nullable = false)
    private boolean enableProfitShare;

    /**
     * 日期
     */
    @Column(nullable = false,name = "profit_date")
    private Date profitDate;

    /**
     * 分成金额
     */
    @Column(nullable = false)
    private Double shareAmount = 0D;

    /**
     * 广告是否达标
     */
    @Column(nullable = false,columnDefinition = "tinyint(1) default 0")
    private Boolean isQualified = Boolean.FALSE;

    public Boolean getIsQualified() {
        return isQualified;
    }

    public void setIsQualified(Boolean qualified) {
        isQualified = qualified;
    }


    public boolean isEnableProfitShare() {
        return enableProfitShare;
    }

    public void setEnableProfitShare(boolean enableProfitShare) {
        this.enableProfitShare = enableProfitShare;
    }

    public String getStoreProfitStatisticId() {
        return storeProfitStatisticId;
    }

    public void setStoreProfitStatisticId(String storeProfitStatisticId) {
        this.storeProfitStatisticId = storeProfitStatisticId;
    }

    public boolean isActived() {
        return actived;
    }

    public void setActived(boolean actived) {
        this.actived = actived;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public StoreInfo getStoreInfo() {
        return storeInfo;
    }

    public void setStoreInfo(StoreInfo storeInfo) {
        this.storeInfo = storeInfo;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public String getAdvertisementId() {
        return advertisementId;
    }

    public void setAdvertisementId(String advertisementId) {
        this.advertisementId = advertisementId;
    }

    public Advertisement getAdvertisement() {
        return advertisement;
    }

    public void setAdvertisement(Advertisement advertisement) {
        this.advertisement = advertisement;
    }

    public Date getProfitDate() {
        return profitDate;
    }

    public void setProfitDate(Date profitDate) {
        this.profitDate = profitDate;
    }

    public Double getShareAmount() {
        return shareAmount;
    }

    public void setShareAmount(Double shareAmount) {
        this.shareAmount = shareAmount;
    }
}
