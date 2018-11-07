package com.sztouyun.advertisingsystem.viewmodel.job;

import io.swagger.annotations.ApiModel;

import java.util.List;

@ApiModel
public class ResponseStoreInfoPageViewModel {
    private int total;

    private List<ResponseStoreInfoViewModel> list;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<ResponseStoreInfoViewModel> getList() {
        return list;
    }

    public void setList(List<ResponseStoreInfoViewModel> list) {
        this.list = list;
    }
}
