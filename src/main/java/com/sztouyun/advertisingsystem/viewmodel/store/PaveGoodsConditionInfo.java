package com.sztouyun.advertisingsystem.viewmodel.store;

import com.sztouyun.advertisingsystem.viewmodel.commodity.CommodityOptionViewModel;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PaveGoodsConditionInfo {
    private Boolean isPaveGoods;

    private List<CommodityOptionViewModel> paveGoodsList = new ArrayList<>();

    public List<CommodityOptionViewModel> getPaveGoodsList() {
        if (isPaveGoods != null && !isPaveGoods) {
            return new ArrayList<>();
        }
        return paveGoodsList;
    }
}
