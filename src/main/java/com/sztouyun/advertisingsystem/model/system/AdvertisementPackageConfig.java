package com.sztouyun.advertisingsystem.model.system;

import com.sztouyun.advertisingsystem.model.BaseModel;
import com.sztouyun.advertisingsystem.model.account.User;
import org.springframework.data.annotation.CreatedBy;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by wenfeng on 2017/8/30.
 */
@Entity
public class AdvertisementPackageConfig extends BaseModel{
    /**
     * 套餐名称
     */
    @Column(nullable = false)
    private String packageName;

    /**
     * A类屏位数
     */
    @Column(nullable = false,columnDefinition = "Integer default 0")
    private Integer amontOfTypeA;

    /**
     * B类屏位数
     */
    @Column(nullable = false,columnDefinition = "Integer default 0")
    private Integer amontOfTypeB;

    /**
     * C类屏位数
     */
    @Column(nullable = false,columnDefinition = "Integer default 0")
    private Integer amontOfTypeC;

    /**
     * D类屏位数
     */
    @Column(nullable = false,columnDefinition = "Integer default 0")
    private Integer amontOfTypeD;

    /**
     * 总价
     */
    @Column(nullable = false)
    private BigDecimal totalCost;

    /**
     * 周期
     */
    @Column(nullable = false)
    private Integer  period;

    /**
     * 是否激活
     */
    @Column(nullable = false)
    private Boolean actived=true;


    @Column(name = "updater_id",updatable = false,length = 36)
    @CreatedBy
    private String updaterId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updater_id",insertable = false,updatable = false)
    private User updater;

    public Integer getAmontOfTypeD() {
        return amontOfTypeD;
    }

    public void setAmontOfTypeD(Integer amontOfTypeD) {
        this.amontOfTypeD = amontOfTypeD;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Integer getAmontOfTypeA() {
        return amontOfTypeA;
    }

    public void setAmontOfTypeA(Integer amontOfTypeA) {
        this.amontOfTypeA = amontOfTypeA;
    }

    public Integer getAmontOfTypeB() {
        return amontOfTypeB;
    }

    public void setAmontOfTypeB(Integer amontOfTypeB) {
        this.amontOfTypeB = amontOfTypeB;
    }

    public Integer getAmontOfTypeC() {
        return amontOfTypeC;
    }

    public void setAmontOfTypeC(Integer amontOfTypeC) {
        this.amontOfTypeC = amontOfTypeC;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }


    public String getUpdaterId() {
        return updaterId;
    }

    public void setUpdaterId(String updaterId) {
        this.updaterId = updaterId;
    }

    public User getUpdater() {
        return updater;
    }

    public Boolean getActived() {
        return actived;
    }

    public void setActived(Boolean actived) {
        this.actived = actived;
    }

    public Integer getTotalAmount(){
        return (amontOfTypeA+amontOfTypeB+amontOfTypeC+amontOfTypeD);
    }
}
