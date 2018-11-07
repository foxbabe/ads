package com.sztouyun.advertisingsystem.model.order;

import com.sztouyun.advertisingsystem.model.BasePartnerModel;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "order_info")
public class OrderInfo extends BasePartnerModel {
    /**
     * 订单编码
     */
    @Column(nullable = false, length = 20)
    private String code;

    /**
     * 订单名称
     */
    @Column(length=128)
    private String name;

    /**
     * 订单金额
     */
    @Column(nullable = false)
    private Integer orderAmount;

    /**
     * 订单总门店数
     */
    @Column(nullable = false)
    private Integer totalStoreCount;

    /**
     * 订单投放天数
     */
    @Column(nullable = false,columnDefinition = "Integer default  0")
    private Integer totalDays=0;

    /**
     * 开始时间
     */
    @Column(nullable = false)
    private Date startTime;

    /**
     * 结束时间
     */
    @Column(nullable = false)
    private Date endTime;

    /**
     * 实际投放开始时间
     */
    @Column
    private Date effectiveStartTime;

    /**
     * 实际投放截止时间
     */
    @Column
    private Date effectiveEndTime;

    /**
     * 实际投放天数
     */
    @Column(nullable = false,columnDefinition = "Integer default  0")
    private Integer effectiveTotalDays=0;

    /**
     * 订单状态
     */
    @Column(nullable = false)
    private Integer orderStatus = OrderStatusEnum.PendingPublishing.getValue();

    @OneToMany(cascade = CascadeType.ALL,fetch= FetchType.LAZY,mappedBy="orderInfo")
    private List<OrderDetail> orderDetails;

    @OneToMany(cascade = CascadeType.ALL,fetch= FetchType.LAZY,mappedBy="orderInfo")
    private List<OrderMaterial> orderMaterials;

    @Transient
    private OrderStatusEnum orderStatusEnum;
    public OrderStatusEnum getOrderStatusEnum(){
        if(orderStatusEnum == null){
            orderStatusEnum = EnumUtils.toEnum(getOrderStatus(),OrderStatusEnum.class);
        }
        return orderStatusEnum;
    }
}
