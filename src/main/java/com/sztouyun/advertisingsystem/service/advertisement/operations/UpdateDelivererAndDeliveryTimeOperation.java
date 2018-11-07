package com.sztouyun.advertisingsystem.service.advertisement.operations;

import com.sztouyun.advertisingsystem.common.operation.IActionOperation;
import com.sztouyun.advertisingsystem.model.advertisement.Advertisement;
import com.sztouyun.advertisingsystem.model.contract.Contract;
import com.sztouyun.advertisingsystem.repository.advertisement.AdvertisementRepository;
import com.sztouyun.advertisingsystem.service.account.AuthenticationService;
import com.sztouyun.advertisingsystem.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UpdateDelivererAndDeliveryTimeOperation implements IActionOperation<Advertisement> {

    @Autowired
    private AdvertisementRepository advertisementRepository;

    @Override
    public void operateAction(Advertisement advertisement) {
        //更新开始投放时间、投放人、投放后的预计截止时间
        Date effectiveStartTime =DateUtils.getDateAccurateToMinute(new Date());
        advertisement.setEffectiveStartTime(effectiveStartTime);
        advertisement.setDeliveryOperatorId(AuthenticationService.getUser().getId());
        Contract contract =advertisement.getContract();
        Date expectedDueDay = DateUtils.addDays(effectiveStartTime,contract.getContractAdvertisementPeriod()-contract.getUsedContractPeriod());
        advertisement.setExpectedDueDay(expectedDueDay);
        advertisementRepository.save(advertisement);
    }
}
