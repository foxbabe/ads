package com.sztouyun.advertisingsystem.model.monitor;

import com.sztouyun.advertisingsystem.model.BaseAutoKeyEntity;
import com.sztouyun.advertisingsystem.model.order.OrderInfo;
import com.sztouyun.advertisingsystem.model.order.OrderMaterial;
import com.sztouyun.advertisingsystem.model.store.StoreInfo;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(indexes = {
        @Index(name = "index_store_date_material",columnList = "store_id,date,order_material_id", unique = true),
        @Index(name = "index_order_id_date",columnList = "order_id,date")
        })
public class OrderDailyDeliveryMonitorStatistic extends BaseAutoKeyEntity {

    @Column(name = "order_id",updatable = false,length = 36)
    private String orderId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id",insertable = false,updatable = false)
    private OrderInfo orderInfo;

    @Column(name = "store_id",updatable = false,length = 36)
    private String storeId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id",insertable = false,updatable = false)
    private StoreInfo storeInfo;

    /**
     * 每日时间
     */
    private Date date;

    /**
     * 每日展示次数
     */
    private Integer displayTimes;

    /**
     *订单与素材的中间表ID
     */
    @Column(name = "order_material_id",updatable = false,length = 36)
    private String orderMaterialId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_material_id",insertable = false,updatable = false)
    private OrderMaterial orderMaterial;


}
