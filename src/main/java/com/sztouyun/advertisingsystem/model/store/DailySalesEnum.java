package com.sztouyun.advertisingsystem.model.store;

import com.sztouyun.advertisingsystem.common.EnumMessage;

public enum DailySalesEnum implements EnumMessage<Integer> {

    OneThousand(1000,"0-1000"),
    TwoThousand(2000,"1001-2000"),
    ThreeThousand(3000,"2001-3000"),
    FourThousand(4000,"3001-4000"),
    FiveThousand(5000,"4001-5000"),
    SixThousand(6000,"5001-6000"),
    SevenThousand(7000,"6001-7000"),
    EightThousand(8000,"7001-8000"),
    NineThousand(9000,"8001-9000"),
    TenThousand(10000,"9001-10000"),
    Infinity(99999,"10000以上");

    private Integer value;
    private String displayName;

    DailySalesEnum(Integer value, String displayName) {
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
