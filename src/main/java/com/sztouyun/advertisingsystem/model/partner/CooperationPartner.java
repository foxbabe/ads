package com.sztouyun.advertisingsystem.model.partner;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sztouyun.advertisingsystem.model.BaseModel;
import com.sztouyun.advertisingsystem.model.account.User;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class CooperationPartner extends BaseModel {

    /**
     * 头像
     */
    @Column(length=128)
    private String headPortrait;

    /**
     * 合作方编号
     */
    @Column(length=128,nullable = false)
    private String code;

    /**
     *合作方名称
     */
    @Column(nullable = false,length=128)
    private String name;

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
     * 邮箱
     */
    @Column(length=128)
    private String mailAddress;

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
    private String addressDetail;


    /**
     * 备注
     */
    @Column(length=2000)
    private String remark;

    /**
     * 所属用户Id
     */
    @Column(length=36,name = "owner_id")
    private String ownerId;


    /**
     * 合作模式
     */
    @Column(nullable = false)
    private Integer cooperationPattern;

    /**
     * 合作模式时长
     */
    @Column
    private Integer duration;

    /**
     * 合作模式时长单位
     */
    @Column
    private Integer durationUnit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id",insertable = false,updatable = false)
    @JsonIgnore
    private User owner;

    private boolean disabled;

    @Column(columnDefinition = "varchar(200) default ''")
    private String apiUrl = "";

    @Column(nullable = false)
    private Integer priority;
}
