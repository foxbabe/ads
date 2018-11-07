package com.sztouyun.advertisingsystem.model.order;

import com.sztouyun.advertisingsystem.model.material.PartnerMaterial;
import com.sztouyun.advertisingsystem.model.system.AdvertisementPositionCategoryEnum;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import com.sztouyun.advertisingsystem.utils.UUIDUtils;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(indexes = {
        @Index(name = "index_order_id",columnList = "order_id")})
public class OrderMaterial implements Serializable{
    @Id
    @Column(nullable = false,length = 36)
    private String id = UUIDUtils.generateOrderedUUID();

    /**
     *广告位种类
     */
    @Column(nullable = false)
    private Integer advertisementPositionCategory;

    @Column(nullable = false)
    private Integer advertisementPositionType;

    @Column(nullable = false)
    private Integer terminalType;

    @Column(name = "order_id",nullable = false, length = 36)
    private String orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id",insertable = false,updatable = false)
    private OrderInfo orderInfo;

    /**
     *合作方素材ID
     */
    @Column(name = "partner_material_id", length = 36)
    private String partnerMaterialId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partner_material_id",insertable = false,updatable = false)
    private PartnerMaterial partnerMaterial;

    /**
     *展示次数(每天)
     */
    @Column
    private Integer  displayTimes;

    /**
     *展示时长(秒)
     */
    @Column
    private Integer duration;

    public void setAdvertisementPositionCategory(Integer advertisementPositionCategory) {
        AdvertisementPositionCategoryEnum advertisementPositionCategoryEnum = EnumUtils.toEnum(advertisementPositionCategory,AdvertisementPositionCategoryEnum.class);
        setAdvertisementPositionType(advertisementPositionCategoryEnum.getAdvertisementPositionType().getValue());
        setTerminalType(advertisementPositionCategoryEnum.getTerminalType().getValue());
        this.advertisementPositionCategory = advertisementPositionCategory;
    }

    private void setAdvertisementPositionType(Integer advertisementPositionType) {
        this.advertisementPositionType = advertisementPositionType;
    }

    private void setTerminalType(Integer terminalType) {
        this.terminalType = terminalType;
    }
}
