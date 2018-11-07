package com.sztouyun.advertisingsystem.viewmodel.store.storePortrait;

import com.sztouyun.advertisingsystem.model.store.EnvironmentTypeEnum;
import lombok.Data;

@Data
public class EnvironmentTypeStoreStatistics {
    /**
     * 环境类型枚举值 {@link EnvironmentTypeEnum}
     */
    private Integer mainEnvironmentType;

    /**
     * 环境类型对应的门店数量
     */
    private long storeNum;
}
