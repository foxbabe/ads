package com.sztouyun.advertisingsystem.model.advertisement;


import com.sztouyun.advertisingsystem.model.contract.ContractAdvertisementPositionConfig;
import com.sztouyun.advertisingsystem.model.system.QRCodePositionEnum;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import com.sztouyun.advertisingsystem.utils.UUIDUtils;
import lombok.Data;

import javax.persistence.*;

/**
 * Created by wenfeng on 2017/10/19.
 */
@Data
@Entity
public class AdvertisementMaterial {
    @Id
    @Column(nullable = false,length = 36)
    private String id = UUIDUtils.generateOrderedUUID();

    /**
     * 广告ID
     */
    @Column(name = "advertisement_id",nullable = false, length = 36)
    private String advertisementId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "advertisement_id",insertable = false,updatable = false)
    private Advertisement Advertisement;

    /**
     * 对应素材id
     */
    @Column(name = "material_id",length = 36)
    private String materialId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material_id",insertable = false,updatable = false)
    private com.sztouyun.advertisingsystem.model.material.Material material;

    /**
     * 对应合同广告配置id
     */
    @Column(name = "position_id",length = 36)
    private String positionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id",insertable = false,updatable = false)
    private ContractAdvertisementPositionConfig contractAdvertisementPositionConfig;

    /**
     * 水平分辨率
     */
    @Column(nullable = false,columnDefinition="int default 0")
    private Integer horizontalResolution;

    /**
     * 垂直分辨率
     */
    @Column(nullable = false,columnDefinition="int default 0")
    private Integer verticalResolution;

    /**
     * 宽度比
     */
    @Column(nullable = false,columnDefinition="int default 0")
    private Integer widthRatio;

    /**
     * 高度比
     */
    @Column(nullable = false,columnDefinition="int default 0")
    private Integer highRatio;

    /**
     * 素材点击Url是否开启
     */
    @Column(nullable = false, columnDefinition = "tinyint(1) default 0")
    private Boolean clickUrlEnable;

    /**
     * 素材点击Url
     */
    @Column(length = 200)
    private String materialClickUrl;

    /**
     * 素材二维码Url是否开启
     */
    @Column(nullable = false, columnDefinition = "tinyint(1) default 0")
    private Boolean qRCodeUrlEnable;

    /**
     * 素材二维码Url
     */
    @Column(length = 200)
    private String materialQRCodeUrl;

    /**
     * 二维码位置
     */
    private Integer qRCodePosition;

    /**
     * 是否记录手机号
     */
    @Column
    private Boolean isNotePhoneNumber;

    public String getImgSpecification() {
        return this.getHorizontalResolution() + "*" + this.getVerticalResolution();
    }

    public QRCodePositionEnum getQRCodePositionEnum() {
        return EnumUtils.toEnum(qRCodePosition, QRCodePositionEnum.class);
    }
}
