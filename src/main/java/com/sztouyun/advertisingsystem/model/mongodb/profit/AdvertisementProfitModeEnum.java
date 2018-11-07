package com.sztouyun.advertisingsystem.model.mongodb.profit;

import com.sztouyun.advertisingsystem.common.EnumMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AdvertisementProfitModeEnum implements EnumMessage<Integer> {
    FixedAmount(0, "固定分成计费"),
    DeliveryEffect(1, "投放效果计费"),
    DeliveryPoint(2, "投放点计费"),
    ActiveDegree(3, "活跃度计费");

    private Integer value;
    private String displayName;
}
