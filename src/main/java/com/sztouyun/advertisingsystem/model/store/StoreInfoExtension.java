package com.sztouyun.advertisingsystem.model.store;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

/**
 * 门店扩展信息
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreInfoExtension {
    @Id
    @GeneratedValue(generator = "pkGenerator")
    @GenericGenerator(name = "pkGenerator", strategy = "foreign", parameters = @org.hibernate.annotations.Parameter(name = "property", value = "storeInfo"))
    @Column(name = "id", nullable = false, unique = true, length = 36)
    private String id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @PrimaryKeyJoinColumn
    private StoreInfo storeInfo;

    /**
     * 门店类型
     */
    @Column
    private int storeFrontType;

    /**
     * 日销售额
     */
    @Column
    private int dailySales;

    /**
     * 装修情况
     */
    @Column
    private int decoration;

    /**
     * 营业面积
     */
    @Column
    private int commercialArea;

    /**
     * 订货比例
     */
    @Column
    private int orderRatio;

    /**
     * 银行卡号
     */
    @Column
    private String bankCard;

    /**
     * 盈利情况
     */
    @Column
    private String profitability;

    /**
     * 居民区数量
     */
    @Column
    private Integer residentialDistrictCount;

    /**
     * 门头照
     */
    @Column
    private String outsidePicture;

    /**
     * 店内照
     */
    @Column
    private String insidePicture;

    /**
     * 收银台照
     */
    @Column
    private String cashRegisterPicture;

    @OneToMany(cascade = CascadeType.ALL,fetch=FetchType.LAZY,mappedBy="storeInfoExtension")
    private List<StorePortrait> storePortraits;
}
