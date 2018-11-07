package com.sztouyun.advertisingsystem.model.store;

import com.sztouyun.advertisingsystem.model.commodity.Commodity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@Table(indexes = {
        @Index(name = "index_store_id_commodity_id",columnList = "store_id,commodity_id")
       })
public class StoreCommodity {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    /**
     * 门店号码
     */
    @Column(name = "store_id",nullable = false, length = 36)
    private String storeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id",insertable = false,updatable = false)
    private StoreInfo storeInfo;

    /**
     * 商品ID
     */
    @Column(name = "commodity_id",nullable = false, length = 36)
    private Long commodityId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commodity_id",insertable = false,updatable = false)
    private Commodity commodity;

    public StoreCommodity(String storeId, Long commodityId) {
        this.storeId = storeId;
        this.commodityId = commodityId;
    }
}
