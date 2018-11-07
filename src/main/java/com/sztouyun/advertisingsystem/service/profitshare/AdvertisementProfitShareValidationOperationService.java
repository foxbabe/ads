package com.sztouyun.advertisingsystem.service.profitshare;

import com.sztouyun.advertisingsystem.common.operation.IOperationCollection;
import com.sztouyun.advertisingsystem.service.profitshare.base.BaseProfitShareValidationOperationService;
import com.sztouyun.advertisingsystem.service.profitshare.operations.ValidateStoreAdvertisementDeliveryLogOperation;
import com.sztouyun.advertisingsystem.service.profitshare.operations.data.AdvertisementValidationRuleData;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdvertisementProfitShareValidationOperationService extends BaseProfitShareValidationOperationService<AdvertisementValidationRuleData> {
    @Override
    protected void onOperating(AdvertisementValidationRuleData ruleData, IOperationCollection<AdvertisementValidationRuleData, Boolean> operationCollection) {
        operationCollection.add(ValidateStoreAdvertisementDeliveryLogOperation.class);
    }

    @Override
    protected Boolean getResult(List<Boolean> resultList) {
        for (Boolean result :resultList){
            if(!result)
                return false;
        }
        return true;
    }
}
