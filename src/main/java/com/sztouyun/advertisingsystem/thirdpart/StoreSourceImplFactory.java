package com.sztouyun.advertisingsystem.thirdpart;

import com.sztouyun.advertisingsystem.model.store.StoreSourceEnum;
import com.sztouyun.advertisingsystem.thirdpart.impl.OmsNewThirdPartImpl;
import com.sztouyun.advertisingsystem.utils.SpringUtil;

public class StoreSourceImplFactory {
    public static IThirdPart getInstance(StoreSourceEnum storeSourceEnum) {
        switch (storeSourceEnum) {
            case NEW_OMS:
                return SpringUtil.getBean(OmsNewThirdPartImpl.class);
            default:
                return null;
        }
    }
}
