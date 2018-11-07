package com.sztouyun.advertisingsystem.model;

import com.sztouyun.advertisingsystem.model.partner.CooperationPartner;

import javax.persistence.*;

@MappedSuperclass
public abstract class BasePartnerModel extends BaseEntity {
    @Column(name = "partner_id",updatable = false,length = 36)
    private String partnerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partner_id",insertable = false,updatable = false)
    private CooperationPartner partner;

    /**
     * 第三方对应的ID
     */
    @Column(name = "third_part_id",length = 50)
    private String thirdPartId;

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public CooperationPartner getPartner() {
        return partner;
    }

    public void setPartner(CooperationPartner partner) {
        this.partner = partner;
    }

    public String getThirdPartId() {
        return thirdPartId;
    }

    public void setThirdPartId(String thirdPartId) {
        this.thirdPartId = thirdPartId;
    }
}
