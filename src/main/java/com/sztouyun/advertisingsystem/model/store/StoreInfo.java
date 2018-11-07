package com.sztouyun.advertisingsystem.model.store;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sztouyun.advertisingsystem.utils.UUIDUtils;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;

/**
 * 门店网点
 */
@Entity
@Data
@Table(indexes = {
        @Index(name = "index_province_id",columnList = "province_id"),
        @Index(name = "index_city_id",columnList = "city_id"),
        @Index(name = "index_region_id",columnList = "region_id"),
        @Index(name = "index_store_no",columnList = "store_no")})
public class StoreInfo {

    @Id
    @Column(nullable = false,length = 36)
    private String id = UUIDUtils.generateOrderedUUID();
    /**
     * 门店编号
     */
    @Column(nullable = false,length = 50,name = "store_no")
    private String storeNo;
    /**
     * 门店名称
     */
    @Column(nullable = false,length = 50)
    private String storeName;
    /**
     * 门店类型（动态计算）
     */
    @Column(name = "store_type")
    private int storeType;
    /**
     * 设备编码
     */
    @Column(nullable = false,length = 50,name = "device_id")
    private String deviceId;
    /**
     * 门店地址
     */
    @Column(nullable = false,length = 100)
    private String storeAddress;
    /**
     * 省份id
     */
    @Column(nullable = false,length=36,name = "province_id")
    private String provinceId;
    /**
     * 城市id
     */
    @Column(nullable = false,length=36,name = "city_id")
    private String cityId;
    /**
     * 区域id
     */
    @Column(nullable = false,length=36,name = "region_id")
    private String regionId;

    /**
     * 交易数量
     */
    @Column(length = 20)
    private Integer transactionCount;

    /**
     * GPS定位更新时间
     */
    @Column
    private Date gpsPositionUpdateTime;

    @Column(nullable = false,updatable = false)
    @CreatedDate
    private Date createdTime = new Date();

    @Column(nullable = false)
    @LastModifiedDate
    private Date updatedTime = new Date();
    /**
     *  已使用广告位数量
     */
    @Column(nullable = false,columnDefinition = "Integer default  0")
    private Integer usedCount = 0;

    /**
     *  是否已选择
     */
    @Transient
    private Integer isChoose;

    /**
     *  开店时间
     */
    @Column
    private Date createdAt;

    /**
     * 是否删除
     */
    @Column(nullable = false,columnDefinition = "tinyint(1) default 0")
    private boolean deleted;

    /**
     * 是否可用
     */
    @Column(nullable = false,columnDefinition = "tinyint(1) default 0")
    private boolean available;

    /**
     * 门店是否达标
     */
    @Column(nullable = false,columnDefinition = "tinyint(1) default 0")
    private Boolean isQualified;

    /**
     * 是否已使用(合同状态为执行中时，门店为已使用)
     */
    @Column(nullable = false,columnDefinition = "tinyint(1) default 0")
    private boolean isUsing;

    /**
     * 门店来源
     */
    @Column(nullable = false,columnDefinition = "int default 1")
    private Integer storeSource;

    @Column(columnDefinition="double default 0 comment '经度'")
    private double longitude;

    @Column(columnDefinition="double default 0 comment '纬度'")
    private double latitude;

    @Transient
    private String oldStoreNo;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "storeInfo", fetch = FetchType.LAZY, optional = false)
    @JsonIgnore
    private StoreInfoExtension storeInfoExtension;

    /**
     * 是否测试门店, 默认否
     */
    @Column(nullable = false,columnDefinition = "tinyint(1) default 0")
    private Boolean isTest = Boolean.FALSE;

    /**
     * 店面照
     */
    @Column
    private String outsidePicture;

    /**
     * 店内照
     */
    @Column
    private String insidePicture;

    public StoreInfo(String id, String storeNo, String storeName, Integer storeType, String storeAddress, String cityId){
        this.id = id;
        this.storeNo = storeNo;
        this.storeName = storeName;
        this.storeType = storeType;
        this.storeAddress = storeAddress;
        this.cityId = cityId;
    }

    public StoreInfo(String id, String storeNo, String storeName, String deviceId, String storeAddress, String cityId, Integer usedCount, Integer isChoose) {
        this.id = id;
        this.storeNo = storeNo;
        this.storeName = storeName;
        this.deviceId = deviceId;
        this.storeAddress = storeAddress;
        this.cityId = cityId;
        this.usedCount = usedCount;
        this.isChoose = isChoose;
    }

    public StoreInfo() {
    }
}
