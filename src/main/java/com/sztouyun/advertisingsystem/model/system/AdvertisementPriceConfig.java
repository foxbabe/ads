package com.sztouyun.advertisingsystem.model.system;

import com.sztouyun.advertisingsystem.model.BaseModel;
import com.sztouyun.advertisingsystem.utils.DateUtils;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Date;

/**
 * Created by wenfeng on 2017/8/4.
 */
@Entity
public class AdvertisementPriceConfig extends BaseModel {


    /**
     * 门店类型
     */
    @Column(nullable = false)
    private Integer storeType;

    /**
     * 单位数量
     */
    @Column(nullable = false)
    private Integer unitQuantity=1;

    /**
     * 周期
     */
    @Column(nullable = false)
    private Integer  period;

    /**
     * 单价
     */
    @Column(nullable = false)
    private Double price;

    /**
     * 展示到页面的开始时间
     */
    @Column(nullable = false,columnDefinition="datetime default '1970-01-01 00:00:000'")
    private Date startTime;

    @Column(nullable = false)
    private Date updatedTime;

    /**
     * 是否激活
     */
    @Column(nullable = false)
    private Boolean actived=true;

    public Integer getStoreType() {
        return storeType;
    }

    public void setStoreType(Integer storeType) {
        this.storeType = storeType;
    }

    public Integer getUnitQuantity() {
        return unitQuantity;
    }

    public void setUnitQuantity(Integer unitQuantity) {
        this.unitQuantity = unitQuantity;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Boolean getActived() {
        return actived;
    }

    public void setActived(Boolean actived) {
        this.actived = actived;
    }

    @Override
    public Date getUpdatedTime() {
        return updatedTime;
    }

    @Override
    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Date getStartTime() {
        if(startTime.getTime()==(DateUtils.getMinDate().getTime())){
            return getCreatedTime();
        }
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
}
