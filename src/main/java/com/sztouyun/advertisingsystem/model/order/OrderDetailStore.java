package com.sztouyun.advertisingsystem.model.order;

import com.sztouyun.advertisingsystem.model.store.StoreInfo;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(indexes = {
        @Index(name = "index_order_detail_id",columnList = "order_detail_id"),
        @Index(name = "index_store_id",columnList = "store_id")})
public class OrderDetailStore {
    public OrderDetailStore() {
    }

    public OrderDetailStore(String orderDetailId, String storeId) {
        this.orderDetailId = orderDetailId;
        this.storeId = storeId;
    }

    @Id
    @Column(nullable = false,length = 36)
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    @Column(name = "order_detail_id",nullable = false, length = 36)
    private String orderDetailId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_detail_id",insertable = false,updatable = false)
    private OrderDetail orderDetail;

    /**
     * 门店ID
     */
    @Column(name = "store_id",nullable = false, length = 36)
    private String storeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id",insertable = false,updatable = false)
    private StoreInfo storeInfo;
}
