package com.sztouyun.advertisingsystem.model.store;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by wenfeng on 2018/3/21.
 */
public class  OmsData<D> {
    @JsonProperty("Total")
    private int total;

    @JsonProperty("List")
    private List<D> list;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<D> getList() {
        return list;
    }

    public void setList(List<D> list) {
        this.list = list;
    }
}
