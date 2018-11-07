package com.sztouyun.advertisingsystem.model.customer;

import com.sztouyun.advertisingsystem.model.BaseModel;
import com.sztouyun.advertisingsystem.model.account.User;
import com.sztouyun.advertisingsystem.model.advertisement.Advertisement;
import com.sztouyun.advertisingsystem.model.contract.Contract;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * Created by szty on 2017/7/25.
 */
@Data
@Entity
@Table(indexes = {
        @Index(name = "index_province_id",columnList = "province_id"),
        @Index(name = "index_city_id",columnList = "city_id"),
        @Index(name = "index_region_id",columnList = "region_id")})
public class Customer extends BaseModel {
    /**
     *客户名称
     */
    @Column(nullable = false,length=128)
    private String name;
    /**
     * 客户编号
     */
    @Column(length=128)
    private String customerNumber;
    /**
     * 联系人
     */
    @Column(nullable = false,length=128)
    private String contacts;
    /**
     * 联系电话
     */
    @Column(nullable = false,length=20)
    private String contactNumber;
    /**
     * 从属行业
     */
    @Column(length=128)
    private String industry;

    /**
     * 从属行业ID
     */
    @Column(length=36)
    private String industryId;

    /**
     * 从属子行业
     */
    @Column(length=128)
    private String subIndustry;

    /**
     * 从属子行业ID
     */
    @Column(length=36)
    private String subIndustryId;

    /**
     * 邮箱
     */
    @Column(length=128)
    private String mailAdress;

    /**
     * 省份id
     */
    @Column(length=36,name = "province_id")
    private String provinceId;

    /**
     * 城市id
     */
    @Column(length=36,name = "city_id")
    private String cityId;

    /**
     * 区域id
     */
    @Column(length=36,name = "region_id")
    private String regionId;

    /**
     * 详细地址
     */
    @Column(length=128)
    private String adressDetail;


    /**
     * 头像
     */
    @Column(length=128)
    private String headPortrait;
    /**
     * 所属用户Id
     */
    @Column(length=36,name = "owner_id")
    private String ownerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id",insertable = false,updatable = false)
    private User owner;

    @OneToMany(fetch=FetchType.LAZY,mappedBy="customer")
    private List<Contract> contracts;

    @OneToMany(fetch=FetchType.LAZY,mappedBy="customer")
    private List<Advertisement> advertisements;

    /**
     * 详细的客户信息扩展懒加载
     */
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "customer", fetch = FetchType.LAZY, optional = false)
    private CustomerExtension customerExtension = new CustomerExtension();
}
