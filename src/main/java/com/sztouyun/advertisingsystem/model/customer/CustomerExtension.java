package com.sztouyun.advertisingsystem.model.customer;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

@Entity
public class CustomerExtension {

    @Id
    @GeneratedValue(generator = "pkGenerator")
    @GenericGenerator(name = "pkGenerator", strategy = "foreign", parameters = @Parameter(name = "property", value = "customer"))
    @Column(name = "id", nullable = false, unique = true, length = 36)
    private String id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @PrimaryKeyJoinColumn
    private Customer customer;

    /**
     * 备注
     */
    @Column(length=2000)
    private String remark;

    /**
     * 甲方名称
     */
    @Column(length = 128)
    private String firstPartyName;

    /**
     * 责任联系人
     */
    @Column(length = 128)
    private String firstPartyResponsibilityPerson;

    /**
     * 合同联系电话
     */
    @Column(length = 128)
    private String firstPartyPhone;

    /**
     * 合同邮箱
     */
    @Column(length = 128)
    private String firstPartyEmail;

    /**
     * 指定送达地址
     */
    @Column(length = 2000)
    private String firstPartyContractReceiveAddress;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getFirstPartyName() {
        return firstPartyName;
    }

    public void setFirstPartyName(String firstPartyName) {
        this.firstPartyName = firstPartyName;
    }

    public String getFirstPartyResponsibilityPerson() {
        return firstPartyResponsibilityPerson;
    }

    public void setFirstPartyResponsibilityPerson(String firstPartyResponsibilityPerson) {
        this.firstPartyResponsibilityPerson = firstPartyResponsibilityPerson;
    }

    public String getFirstPartyPhone() {
        return firstPartyPhone;
    }

    public void setFirstPartyPhone(String firstPartyPhone) {
        this.firstPartyPhone = firstPartyPhone;
    }

    public String getFirstPartyEmail() {
        return firstPartyEmail;
    }

    public void setFirstPartyEmail(String firstPartyEmail) {
        this.firstPartyEmail = firstPartyEmail;
    }

    public String getFirstPartyContractReceiveAddress() {
        return firstPartyContractReceiveAddress;
    }

    public void setFirstPartyContractReceiveAddress(String firstPartyContractReceiveAddress) {
        this.firstPartyContractReceiveAddress = firstPartyContractReceiveAddress;
    }
}
