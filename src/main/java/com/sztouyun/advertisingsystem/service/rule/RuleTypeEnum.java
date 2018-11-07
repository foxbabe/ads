package com.sztouyun.advertisingsystem.service.rule;

import com.sztouyun.advertisingsystem.common.EnumMessage;
import com.sztouyun.advertisingsystem.common.TimeSpan;

/**
 * Created by RiberLi on 2018/1/9 0009.
 */
public enum RuleTypeEnum implements EnumMessage<Integer> {
    ValidateStoreOrder(2,"验证门店订单"),
    ValidateStoreOpeningTime(3,"验证门店营业时间", TimeSpan.class),
    ValidateStoreAdvertisementDeliveryLog(4,"验证门店广告投放日志"),



    CalculateStoreAdvertisementProfitShare(101,"计算门店广告分成"),
    ;

    final private Integer value;
    final  private String displayName;
    final private Class<?> extendClass;

    RuleTypeEnum(Integer value,String displayName) {
        this.value = value;
        this.displayName = displayName;
        this.extendClass = Void.class;
    }

    RuleTypeEnum(Integer value,String displayName,Class<?> extendClass) {
        this.value = value;
        this.displayName = displayName;
        this.extendClass = extendClass;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    public Class<?> getExtendClass() {
        return extendClass;
    }
}
