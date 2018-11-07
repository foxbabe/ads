package com.sztouyun.advertisingsystem.model.store;

import com.sztouyun.advertisingsystem.common.EnumMessage;

public enum StorePortraitEnum implements EnumMessage<Integer> {

    storeFrontType(1,"门店类型",StoreFrontTypeEnum.class),
    dailySales(2,"日销售额",DailySalesEnum.class),
    decoration(3,"装修情况",DecorationEnum.class),
    surroundingsDistrict(4,"周边环境",SurroundingsDistrictEnum.class),
    commercialArea(5,"营业面积(㎡)",CommercialAreaEnum.class),
    orderRatio(6,"订货比例",OrderRatioEnum.class);

    private Integer value;
    private String displayName;
    private Class<? extends EnumMessage<Integer>>  valueEnum;

    StorePortraitEnum(Integer value, String displayName , Class<? extends EnumMessage<Integer>> valueEnum) {
        this.value = value;
        this.displayName = displayName;
        this.valueEnum = valueEnum;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    public Class<? extends EnumMessage<Integer>> getValueEnum() {
        return valueEnum;
    }

    public void setValueEnum(Class<? extends EnumMessage<Integer>> valueEnum) {
        this.valueEnum = valueEnum;
    }
}
