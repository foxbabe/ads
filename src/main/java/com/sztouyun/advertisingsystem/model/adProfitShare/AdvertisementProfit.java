package com.sztouyun.advertisingsystem.model.adProfitShare;

import com.sztouyun.advertisingsystem.model.advertisement.Advertisement;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * 广告分成结算汇总
 */
@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
public class AdvertisementProfit {

    /**
     * 广告ID
     */
    @Id
    @GeneratedValue(generator = "pkGenerator")
    @GenericGenerator(name = "pkGenerator", strategy = "foreign", parameters = @Parameter(name = "property", value = "advertisement"))
    @Column(name = "id",nullable = false, length = 36)
    private String id;

    @OneToOne(fetch = FetchType.LAZY,optional = false)
    @PrimaryKeyJoinColumn(name = "id")
    private Advertisement advertisement;

    /**
     * 分成金额
     */
    @Column(nullable = false)
    private Double shareAmount = 0D;

    /**
     * 已结算
     */
    @Column(nullable = false)
    private Double settledAmount = 0D;

    /**
     * 未结算
     */
    @Column(nullable = false)
    private Double unsettledAmount = 0D;

    @Column(nullable = false,updatable = false)
    @CreatedDate
    private Date createdTime;

    @Column(nullable = false)
    @LastModifiedDate
    private Date updatedTime;

}