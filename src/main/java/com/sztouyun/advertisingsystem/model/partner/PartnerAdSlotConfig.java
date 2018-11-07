package com.sztouyun.advertisingsystem.model.partner;

import com.sztouyun.advertisingsystem.model.BaseAutoKeyEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * 合作方广告位配置
 */
@Data
@Entity
public class PartnerAdSlotConfig  extends BaseAutoKeyEntity{
    @Column(name = "partner_id",updatable = false,length = 36)
    private String partnerId;

    @Column(length = 36)
    private String updaterId;

    /**
     * 合作方广告位 {@link PartnerAdSlotEnum}
     */
    @Column(nullable = false)
    private Integer adSlot;

    @Column(nullable = false)
    private Boolean enabled =true;

    @Column(nullable = false)
    private Integer priority;
}
