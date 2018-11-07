package com.sztouyun.advertisingsystem.model.advertisement;

import com.sztouyun.advertisingsystem.model.BaseModel;
import com.sztouyun.advertisingsystem.utils.EnumUtils;

import javax.persistence.*;

@Entity
public class AdvertisementOperationLog extends BaseModel {

    public AdvertisementOperationLog(){

    }

    public AdvertisementOperationLog(String advertisementId, Integer operation, boolean successed, String remark) {
        this.advertisementId = advertisementId;
        this.operation = operation;
        this.successed = successed;
        this.remark = remark;
    }

    @Column(name = "advertisement_id", nullable = false, length = 36)
    private String advertisementId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "advertisement_id", insertable = false, updatable = false)
    private Advertisement advertisement;

    @Column(nullable = false)
    private Integer operation;

    @Column(nullable = false)
    private boolean successed;

    @Column(nullable = false)
    private boolean finishContract = false;

    @Column(length = 2000)
    private String remark;

    @Transient
    private  boolean changeByContract;
    @Transient
    private boolean isAutoTakeOff = false;
    @Transient
    private boolean affectOtherAdvertisements;

    public boolean isAffectOtherAdvertisements() {
        return affectOtherAdvertisements;
    }

    public void setAffectOtherAdvertisements(boolean affectOtherAdvertisements) {
        this.affectOtherAdvertisements = affectOtherAdvertisements;
    }

    public boolean getChangeByContract() {
        return changeByContract;
    }

    public void setChangeByContract(boolean changeByContract) {
        this.changeByContract = changeByContract;
    }

    public boolean isAutoTakeOff() {
        return isAutoTakeOff;
    }

    public void setAutoTakeOff(boolean autoTakeOff) {
        isAutoTakeOff = autoTakeOff;
    }

    @Transient
    private AdvertisementStatusEnum advertisementStatusEnum;

    public AdvertisementStatusEnum getAdvertisementStatusEnum() {
        if (advertisementStatusEnum == null) {
            advertisementStatusEnum = EnumUtils.toEnum(getOperation(), AdvertisementStatusEnum.class);
        }

        return advertisementStatusEnum;
    }

    public void setAdvertisementStatusEnum(AdvertisementStatusEnum advertisementStatusEnum) {
        this.advertisementStatusEnum = advertisementStatusEnum;
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

    public Integer getOperation() {
        return operation;
    }

    public void setOperation(Integer operation) {
        this.operation = operation;
    }

    public boolean isSuccessed() {
        return successed;
    }

    public void setSuccessed(boolean successed) {
        this.successed = successed;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean isFinishContract() {
        return finishContract;
    }

    public void setFinishContract(boolean finishContract) {
        this.finishContract = finishContract;
    }

    public AdvertisementOperationEnum  getAdvertisementOperationEnum(){
        return EnumUtils.toEnum(getOperation(),AdvertisementOperationEnum.class);
    }
}
