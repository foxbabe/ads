package com.sztouyun.advertisingsystem.model.customer;

import com.sztouyun.advertisingsystem.model.BaseModel;
import com.sztouyun.advertisingsystem.model.account.User;
import com.sztouyun.advertisingsystem.utils.UUIDUtils;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by wenfeng on 2017/10/10.
 */
@Entity
@Table(indexes = {
        @Index(name="index_customer_id", columnList = "customer_id")
})
public class CustomerVisit extends BaseModel{
    /**
     * 客户ID
     */
    @Column(name = "customer_id",nullable = false,updatable = false,length = 36)
    private String customerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id",insertable = false,updatable = false)
    private Customer customer;


    @Column(nullable = false)
    private Date visitTime;

    @Column(nullable = false,length = 128)
    private String address;

    /**
     * 客户名称
     */
    @Column(nullable = false,length = 128)
    private String customerName;

    /**
     * 客户联系人
     */
    @Column(nullable = false,length = 50)
    private String customerContacts;

    /**
     * 客户联系电话
     */
    @Column(nullable = false,length = 20)
    private String customerContactNumber;

    /**
     * 客户邮箱
     */
    @Column(nullable = false,length = 128)
    private String customerEmail;

    @Column
    private Boolean hasIntention;

    @Column
    private Boolean isVital;

    @Column(nullable = false,length = 2048)
    private String remark;

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

    public Date getVisitTime() {
        return visitTime;
    }

    public void setVisitTime(Date visitTime) {
        this.visitTime = visitTime;
    }

    public Boolean getHasIntention() {
        return hasIntention;
    }

    public void setHasIntention(Boolean hasIntention) {
        this.hasIntention = hasIntention;
    }

    public Boolean getIsVital() {
        return isVital;
    }

    public void setIsVital(Boolean isVital) {
        this.isVital = isVital;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerContacts() {
        return customerContacts;
    }

    public void setCustomerContacts(String customerContacts) {
        this.customerContacts = customerContacts;
    }

    public String getCustomerContactNumber() {
        return customerContactNumber;
    }

    public void setCustomerContactNumber(String customerContactNumber) {
        this.customerContactNumber = customerContactNumber;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }
}
