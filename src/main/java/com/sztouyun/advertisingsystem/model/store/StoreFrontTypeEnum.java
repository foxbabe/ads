package com.sztouyun.advertisingsystem.model.store;

import com.sztouyun.advertisingsystem.common.EnumMessage;

public enum StoreFrontTypeEnum implements EnumMessage<Integer> {

    LiquorStore(1,"烟酒专卖店"),
    VarietyStore(2,"杂货店"),
    VegetableStore(3,"菜场店"),
    FranchiseStore(4,"超市加盟店"),
    ConvenienceStore(5,"便利店"),
    ImportFoodStore(6,"进口食品小店"),
    BoutiqueStore(7,"精品超市店"),
    WholesaleStore(8,"批发店"),
    StationerStore(9,"文具店");

    private Integer value;
    private String displayName;

    StoreFrontTypeEnum(Integer value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }
}
