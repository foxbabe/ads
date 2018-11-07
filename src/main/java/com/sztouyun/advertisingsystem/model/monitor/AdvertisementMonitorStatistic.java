package com.sztouyun.advertisingsystem.model.monitor;

import com.sztouyun.advertisingsystem.model.advertisement.Advertisement;
import com.sztouyun.advertisingsystem.model.contract.Contract;
import com.sztouyun.advertisingsystem.model.customer.Customer;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by wenfeng on 2017/10/30.
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
public class AdvertisementMonitorStatistic {
    @Id
    @Column(name = "id", nullable = false, unique = true, length = 36)
    private String id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @PrimaryKeyJoinColumn(name = "id")
    private Advertisement advertisement;

    /**
     * 合同ID
     */
    @Column(name = "contract_id",nullable = false,updatable = false,length = 36)
    private String contractId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id",insertable = false,updatable = false)
    private Contract contract;

    /**
     * 客户ID
     */
    @Column(name = "customer_id",nullable = false,updatable = false,length = 36)
    private String customerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id",insertable = false,updatable = false)
    private Customer customer;

    /**
     * 展示时长
     */
    @Column(nullable = false,columnDefinition = "Integer default  0")
    private Integer duration;

    /**
     * 展示频次
     */
    @Column(nullable = false,columnDefinition = "Integer default  0")
    private Integer displayFrequency;

    /**
     * 预计计总播放次数
     */
    @Column(nullable = false,columnDefinition = "bigint(20) default  0")
    private Long totalDisplayTimes=0L;

    /**
     * 投放城市总数
     */
    @Column(nullable = false,columnDefinition = "Integer default  0")
    private Integer totalCityCount;

    /**
     * 投放门店总数
     */
    @Column(nullable = false,columnDefinition = "Integer default  0")
    private Integer totalStoreCount;


    /**
     * 投放城市
     */
    @Column(nullable = false,length = 2048)
    private String deliveryCities;


    /**
     * 投放素材
     */
    @Column(nullable = false)
    private String materials;


    /**
     * 投放开始时间
     */
    @Column(nullable = false,updatable = false)
    @CreatedDate
    private Date createdTime;

    @Column(nullable = false)
    private Date updatedTime = new Date();


    /**
     * 投放结束时间
     */
    @Column
    private Date endTime;


    /**
     * 累计播放次数
     */
    @Column(nullable = false,columnDefinition = "bigint(20) default  0")
    private Long displayTimes=0L;


    public AdvertisementMonitorStatistic(){

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }


    public Long getDisplayTimes() {
        return displayTimes;
    }

    public void setDisplayTimes(Long displayTimes) {
        this.displayTimes = displayTimes;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Long getTotalDisplayTimes() {
        return totalDisplayTimes;
    }

    public void setTotalDisplayTimes(Long totalDisplayTimes) {
        this.totalDisplayTimes = totalDisplayTimes;
    }

    public Integer getTotalCityCount() {
        return totalCityCount;
    }

    public void setTotalCityCount(Integer totalCityCount) {
        this.totalCityCount = totalCityCount;
    }

    public Integer getTotalStoreCount() {
        return totalStoreCount;
    }

    public void setTotalStoreCount(Integer totalStoreCount) {
        this.totalStoreCount = totalStoreCount;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }


    public Advertisement getAdvertisement() {
        return advertisement;
    }

    public void setAdvertisement(Advertisement advertisement) {
        this.advertisement = advertisement;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getDisplayFrequency() {
        return displayFrequency;
    }

    public void setDisplayFrequency(Integer displayFrequency) {
        this.displayFrequency = displayFrequency;
    }

    public String getDeliveryCities() {
        return deliveryCities;
    }

    public void setDeliveryCities(String deliveryCities) {
        this.deliveryCities = deliveryCities;
    }

    public String getMaterials() {
        return materials;
    }

    public void setMaterials(String materials) {
        this.materials = materials;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
