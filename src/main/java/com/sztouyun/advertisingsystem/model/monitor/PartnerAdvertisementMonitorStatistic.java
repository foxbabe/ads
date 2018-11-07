package com.sztouyun.advertisingsystem.model.monitor;

import com.sztouyun.advertisingsystem.model.partner.CooperationPartner;
import com.sztouyun.advertisingsystem.model.order.OrderInfo;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class PartnerAdvertisementMonitorStatistic {

    @Id
    @Column(name = "id", nullable = false, unique = true, length = 36)
    private String id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @PrimaryKeyJoinColumn(name = "id")
    private OrderInfo orderInfo;

    /**
     * 预计计总播放次数
     */
    @Column(nullable = false,columnDefinition = "Integer default  0")
    private Integer totalDisplayTimes=0;

    /**
     * 累计播放次数
     */
    @Column(nullable = false,columnDefinition = "Integer default  0")
    private Integer displayTimes=0;

    /**
     * 创建时间
     */
    @Column(nullable = false,updatable = false)
    @CreatedDate
    private Date createdTime;

    @Column(nullable = false)
    @LastModifiedDate
    private Date updatedTime = new Date();

    @Column(name = "partner_id",updatable = false,length = 36)
    private String partnerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partner_id",insertable = false,updatable = false)
    private CooperationPartner partner;

    public PartnerAdvertisementMonitorStatistic(String id, Integer totalDisplayTimes, String partnerId) {
        this.id = id;
        this.totalDisplayTimes = totalDisplayTimes;
        this.partnerId = partnerId;
    }

    public PartnerAdvertisementMonitorStatistic() {
    }
}
