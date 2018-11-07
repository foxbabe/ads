package com.sztouyun.advertisingsystem.model.customerStore;

import com.sztouyun.advertisingsystem.viewmodel.customerStore.BaseImportInfo;
import lombok.Data;

import javax.persistence.*;

/**
 * Created by szty on 2018/6/13.
 */
@Entity
@Data
public class CustomerStoreImportTemp{
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    /**
     * 门店编号
     */
    @Column(nullable = false,length = 50)
    private String storeNo;

    /**
     * 操作ID
     */
    @Column(nullable = false,length = 36)
    private String oid;

    /**
     * 业务ID
     */
    @Column(nullable = false,length = 36)
    private String bid;

    /**
     * 门店名称
     */
    @Column(nullable = false,length = 50)
    private String storeName;

    /**
     * 城市
     */
    @Column(nullable = false,length=36)
    private String cityName;

    /**
     * 地区
     */
    @Column(nullable = false,length=36)
    private String regionName;

    /**
     * 门店地址
     */
    @Column(nullable = false,length = 100)
    private String storeAddress;

    /**
     * 设备编码
     */
    @Column(nullable = false,length = 50)
    private String deviceId;

    @Column(nullable = false,columnDefinition = "TINYINT(1) default 0")
    private boolean duplicate;
}
