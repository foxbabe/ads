package com.sztouyun.advertisingsystem.viewmodel.system;

import com.sztouyun.advertisingsystem.model.system.Area;

public class AreaStoreInfo extends Area {

    private Integer storeCount;

    public Integer getStoreCount() {
        return storeCount;
    }

    public void setStoreCount(Integer storeCount) {
        this.storeCount = storeCount;
    }

    public AreaStoreInfo() {
        super();
    }

    public AreaStoreInfo(Integer storeCount, String name, String id, String parentId) {
        super(name,id,parentId);
        this.storeCount = storeCount;
    }
}
