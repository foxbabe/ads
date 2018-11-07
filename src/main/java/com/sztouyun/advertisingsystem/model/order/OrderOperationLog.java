package com.sztouyun.advertisingsystem.model.order;

import com.sztouyun.advertisingsystem.model.BaseModel;
import com.sztouyun.advertisingsystem.model.material.PartnerMaterial;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class OrderOperationLog extends BaseModel {
    /**
     * 订单id
     */
    @Column(name = "order_id",nullable = false, length = 36)
    private String orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id",insertable = false,updatable = false)
    private OrderInfo orderInfo;

    /**
     * 操作
     */
    @Column(nullable = false)
    private Integer operation;

    /**
     * 是否成功
     */
    @Column(nullable = false)
    private boolean successed=true;

    /**
     * 备注
     */
    @Column(length = 2000)
    private String remark;

    /**
     * 第三方单据ID
     */
    @Transient
    private String thirdPartId;

    /**
     * 合作方ID
     */
    @Transient
    private String partnerId;

    /**
     * 第三方的素材ID
     */
    @Transient
    private String thirdPartMaterialId;

    /**
     * 合作方素材
     */
    @Transient
    private PartnerMaterial partnerMaterial;


    @Transient
    private OrderOperationEnum orderOperationEnum;

    public OrderOperationEnum getOrderOperationEnum(){
        if(orderOperationEnum ==null){
            orderOperationEnum = EnumUtils.toEnum(getOperation(),OrderOperationEnum.class);
        }
        return orderOperationEnum;
    }

    public OrderOperationLog() {
    }

    public OrderOperationLog(String orderId, Integer operation) {
        this.orderId = orderId;
        this.operation = operation;
    }
}
