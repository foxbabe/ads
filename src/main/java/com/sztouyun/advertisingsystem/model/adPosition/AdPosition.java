package com.sztouyun.advertisingsystem.model.adPosition;

import com.sztouyun.advertisingsystem.model.BaseModel;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class AdPosition extends BaseModel {

    /**
     * 门店类型
     */
    @Column
    private Integer storeType;
    /**
     * 最大广告数量
     */
    @Column
    private Integer adCount;

    public Integer getStoreType() {
        return storeType;
    }

    public void setStoreType(Integer storeType) {
        this.storeType = storeType;
    }

    public Integer getAdCount() {
        return adCount;
    }

    public void setAdCount(Integer adCount) {
        this.adCount = adCount;
    }
}
