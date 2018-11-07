package com.sztouyun.advertisingsystem.model.partner.advertisement;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(indexes = {
        @Index(name="index_md5_partner_advertisement_id", columnList = "md5,partner_advertisement_id", unique = true)
})
public class PartnerAdvertisementMaterial {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @Column(name = "partner_advertisement_id", nullable = false, length = 36)
    private String partnerAdvertisementId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partner_advertisement_id",insertable = false,updatable = false)
    private PartnerAdvertisement partnerAdvertisement;

    @Column(nullable = false)
    private Integer materialType;

    @Column(length = 50)
    private String materialSpecification = "";

    @Column(length = 50)
    private String materialSize = "";

    @Column(nullable = false,length = 1024)
    private String originalUrl;

    @Column( nullable = false,length = 255)
    private String url = "";

    @Column(name = "md5", nullable = false, length = 32)
    private String md5;

    @Column
    private Integer duration;
}
