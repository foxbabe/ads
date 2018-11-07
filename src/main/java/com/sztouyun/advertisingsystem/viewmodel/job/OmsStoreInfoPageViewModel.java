package com.sztouyun.advertisingsystem.viewmodel.job;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class OmsStoreInfoPageViewModel {
    @JsonProperty("Total")
    private int total;

    @JsonProperty("List")
    private List<OmsStoreInfoViewModel> list;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<OmsStoreInfoViewModel> getList() {
        return list;
    }

    public void setList(List<OmsStoreInfoViewModel> list) {
        this.list = list;
    }
}
