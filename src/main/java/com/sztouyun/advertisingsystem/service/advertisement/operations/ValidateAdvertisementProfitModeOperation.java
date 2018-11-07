package com.sztouyun.advertisingsystem.service.advertisement.operations;

import com.sztouyun.advertisingsystem.common.operation.IActionOperation;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.advertisement.Advertisement;
import com.sztouyun.advertisingsystem.model.mongodb.profit.AdvertisementProfitModeEnum;
import com.sztouyun.advertisingsystem.service.advertisement.AdvertisementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValidateAdvertisementProfitModeOperation implements IActionOperation<Advertisement> {
    @Autowired
    private AdvertisementService advertisementService;
    @Override
    public void operateAction(Advertisement advertisement) {
        if(!advertisement.isEnableProfitShare())
            return;
        if(advertisement.getMode() == null
           || advertisement.getMode().equals(AdvertisementProfitModeEnum.FixedAmount.getValue())
           || !advertisementService.existsAdvertisementProfitConfig(advertisement.getId())
           )
            throw new BusinessException("分成模式不能为空");

    }
}
