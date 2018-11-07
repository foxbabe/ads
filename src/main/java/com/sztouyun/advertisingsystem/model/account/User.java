package com.sztouyun.advertisingsystem.model.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sztouyun.advertisingsystem.model.common.RoleTypeEnum;
import com.sztouyun.advertisingsystem.model.customer.Customer;
import com.sztouyun.advertisingsystem.model.system.Organization;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import org.springframework.util.StringUtils;

import javax.persistence.*;


@Entity
@Table(indexes = {@Index(name = "index_creator_id",columnList = "creator_id")})
@JsonIgnoreProperties(ignoreUnknown = true)
public class User extends BaseUser{
    @Transient
    private Long customerCount;

    @Transient
    private Long signedContractCount;

    @Transient
    private Long cooperationPartnerCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", insertable = false, updatable = false)
    @JsonIgnore
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", insertable = false, updatable = false)
    @JsonIgnore
    private Organization organization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id",insertable = false,updatable = false)
    @JsonIgnore
    private Customer customer;

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        if (StringUtils.isEmpty(organizationId)) {
            this.organizationId = null;
        } else {
            this.organizationId = organizationId;
        }
    }

    public Organization getOrganization() {
        return organization;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @JsonIgnore
    public RoleTypeEnum getRoleTypeEnum() {
        if (isAdmin())
            return RoleTypeEnum.Admin;

        Role role = getRole();
        if (role == null)
            return RoleTypeEnum.SaleMan;
        return EnumUtils.toEnum(role.getRoleType(), RoleTypeEnum.class);
    }

    public boolean isEnabled() {
        return isEnableFlag();
    }

    public Long getCustomerCount() {
        return customerCount;
    }

    public void setCustomerCount(Long customerCount) {
        this.customerCount = customerCount;
    }

    public Long getSignedContractCount() {
        return signedContractCount;
    }

    public void setSignedContractCount(Long signedContractCount) {
        this.signedContractCount = signedContractCount;
    }

    public Long getCooperationPartnerCount() {
        return cooperationPartnerCount;
    }

    public void setCooperationPartnerCount(Long cooperationPartnerCount) {
        this.cooperationPartnerCount = cooperationPartnerCount;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
