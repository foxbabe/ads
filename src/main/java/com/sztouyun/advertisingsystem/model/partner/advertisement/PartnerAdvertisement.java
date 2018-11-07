package com.sztouyun.advertisingsystem.model.partner.advertisement;

import com.sztouyun.advertisingsystem.model.BasePartnerModel;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Data
@Entity
@Table(indexes = {
        @Index(name = "index_partner_id_third_part_id",columnList = "partner_id,third_part_id")})
public class PartnerAdvertisement extends BasePartnerModel {
    @Column(nullable = false)
    private Integer advertisementStatus=PartnerAdvertisementStatusEnum.Delivering.getValue();

    @Column(nullable = false,columnDefinition = "int default 0")
    private Integer materialType = 0;

    @Column(length=128)
    private String name;
}
