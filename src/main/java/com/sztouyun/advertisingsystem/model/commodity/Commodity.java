package com.sztouyun.advertisingsystem.model.commodity;


import com.sztouyun.advertisingsystem.model.BaseAutoKeyEntity;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Commodity extends BaseAutoKeyEntity {
    @Column(nullable = false, length = 50)
    private String code;

    @Column(nullable = false, length = 128)
    private String name;

    @Column(name = "supplier_id",nullable = false,length = 36)
    private Long supplierId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id",insertable = false,updatable = false)
    private Supplier supplier;

    @Column(name = "commodity_type_id",nullable = false,length = 36)
    private Long commodityTypeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commodity_type_id",insertable = false,updatable = false)
    private CommodityType commodityType;
}
