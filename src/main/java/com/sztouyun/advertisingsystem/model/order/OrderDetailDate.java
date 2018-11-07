package com.sztouyun.advertisingsystem.model.order;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(indexes = {
        @Index(name = "index_order_detail_id",columnList = "order_detail_id"),
        @Index(name = "index_date",columnList = "date")})
public class OrderDetailDate {
    public OrderDetailDate() {
    }

    public OrderDetailDate(String orderDetailId, Date date) {
        this.orderDetailId = orderDetailId;
        this.date = date;
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

    @Column(name = "date",nullable = false)
    private Date date;
}
