package com.sztouyun.advertisingsystem.model.customerStore;

import com.sztouyun.advertisingsystem.model.store.StoreInfo;
import lombok.Data;

import javax.persistence.*;

/**
 * Created by szty on 2018/5/15.
 */
@Entity
@Data
@Table(indexes = {
        @Index(name="index_customerStorePlanId_store_id", columnList = "customer_store_plan_id,store_id")
})
public class CustomerStorePlanDetail {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @Column(name = "customer_store_plan_id",nullable = false,updatable = false,length = 36)
    private String customerStorePlanId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_store_plan_id",insertable = false,updatable = false)
    private CustomerStorePlan customerStorePlan;

    @Column(name = "store_id",nullable = false, length = 36)
    private String storeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id",insertable = false,updatable = false)
    private StoreInfo storeInfo;
}
