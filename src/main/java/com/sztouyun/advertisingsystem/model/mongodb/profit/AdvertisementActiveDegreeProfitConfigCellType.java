package com.sztouyun.advertisingsystem.model.mongodb.profit;

import com.sztouyun.advertisingsystem.common.IntegerSpan;
import com.sztouyun.advertisingsystem.common.datatable.ICellType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;

@Getter
@AllArgsConstructor
public enum AdvertisementActiveDegreeProfitConfigCellType implements ICellType {
    Text(0, "文本", String.class, "金额"),
    StoreOrderSpan(1, "门店订单区间", IntegerSpan.class, new IntegerSpan(0, 9)),
    StoreMinBootTime(2, "门店最小开机时长", Integer.class, 12),
    AreaList(3, "地区列表", HashMap.class, new HashMap<String,String>() {{
        put("3", "上海");
        put("5", "河北");
        put("6", "山西");
    }}),
    StoreOrderProfitAmount(4, "门店订单分成金额", Double.class, 0.33),
    StoreBootTimeProfitAmount(5, "门店开机分成金额", Double.class, 0.5);

    private Integer value;
    private String displayName;
    private Class<?> dataClass;
    private Object demoData;
}