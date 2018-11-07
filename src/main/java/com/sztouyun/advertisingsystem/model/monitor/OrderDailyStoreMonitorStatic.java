package com.sztouyun.advertisingsystem.model.monitor;

import com.sztouyun.advertisingsystem.model.BaseAutoKeyEntity;
import com.sztouyun.advertisingsystem.model.order.OrderInfo;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by wenfeng on 2018/3/8.
 */
@Data
@Entity
@Table(indexes = {
        @Index(name = "index_order_date",columnList = "order_id,date ",unique = true)
})
public class OrderDailyStoreMonitorStatic extends BaseAutoKeyEntity {
    @Column(name = "order_id",updatable = false,length = 36)
    private String orderId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id",insertable = false,updatable = false)
    private OrderInfo orderInfo;
    /**
     * 每日时间
     */
    private Date date;

    /**
     * 投放门店数
     */
    private Integer deliveryStoreCount=0;

    /**
     * 可用门店数
     */
    private Integer availableStoreCount;

    /**
     * 已激活门店数
     *
     */
    private Integer activeStoreCount;

    /**
     * 每天实际展示次数
     */
    private Integer displayTimes=0;

}
