package com.sztouyun.advertisingsystem.model.order;

import com.sztouyun.advertisingsystem.utils.UUIDUtils;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(indexes = {
        @Index(name = "index_order_id",columnList = "order_id")})
public class OrderDetail {

    public OrderDetail() {
    }

    public OrderDetail(String orderId) {
        this.orderId = orderId;
    }

    @Id
    @Column(nullable = false,length = 36)
    private String id = UUIDUtils.generateOrderedUUID();

    @Column(name = "order_id",nullable = false, length = 36)
    private String orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id",insertable = false,updatable = false)
    private OrderInfo orderInfo;

    @Column(nullable = false)
    private int storeCount;

    @OneToMany(cascade = CascadeType.ALL,fetch=FetchType.LAZY,mappedBy="orderDetail")
    private List<OrderDetailDate> orderDetailDates;

    @OneToMany(cascade = CascadeType.ALL,fetch=FetchType.LAZY,mappedBy="orderDetail")
    private List<OrderDetailStore> orderDetailStores;
}
