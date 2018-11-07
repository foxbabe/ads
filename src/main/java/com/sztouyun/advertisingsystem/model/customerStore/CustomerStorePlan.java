package com.sztouyun.advertisingsystem.model.customerStore;

import com.sztouyun.advertisingsystem.model.BaseModel;
import com.sztouyun.advertisingsystem.model.customer.Customer;
import lombok.Data;

import javax.persistence.*;

/**
 * Created by szty on 2018/5/15.
 */
@Entity
@Data
@Table(indexes = {
        @Index(name="index_customer_id", columnList = "customer_id")
})
public class CustomerStorePlan extends BaseModel {
    /**
     * 客户ID
     */
    @Column(name = "customer_id",nullable = false,updatable = false,length = 36)
    private String customerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id",insertable = false,updatable = false)
    private Customer customer;

    @Column(nullable = false)
    private String  code;

    @Column(nullable = false)
    private Integer storeCount=0;

    @Column(nullable = false)
    private Integer cityCount=0;
}
