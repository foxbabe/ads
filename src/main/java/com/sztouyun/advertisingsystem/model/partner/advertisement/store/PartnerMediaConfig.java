package com.sztouyun.advertisingsystem.model.partner.advertisement.store;

import com.sztouyun.advertisingsystem.model.BaseEntity;
import com.sztouyun.advertisingsystem.model.partner.CooperationPartner;
import com.sztouyun.advertisingsystem.model.store.StoreInfo;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(indexes = {
        @Index(name = "index_partner_id_store_id",columnList = "partner_id,store_id")})
public class PartnerMediaConfig extends BaseEntity {

    @Column(name = "partner_id",updatable = false,length = 36)
    private String partnerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partner_id",insertable = false,updatable = false)
    private CooperationPartner partner;

    @Column(name = "store_id",nullable = false, length = 36)
    private String storeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id",insertable = false,updatable = false)
    private StoreInfo storeInfo;
}
