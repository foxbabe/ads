package com.sztouyun.advertisingsystem.service.profitshare;

import com.sztouyun.advertisingsystem.common.operation.IOperationCollection;
import com.sztouyun.advertisingsystem.service.profitshare.base.BaseProfitShareValidationOperationService;
import com.sztouyun.advertisingsystem.service.profitshare.operations.ValidateStoreOpeningTimeOperation;
import com.sztouyun.advertisingsystem.service.profitshare.operations.ValidateStoreOrderOperation;
import com.sztouyun.advertisingsystem.service.profitshare.operations.data.StoreValidationRuleData;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreProfitShareValidationOperationService extends BaseProfitShareValidationOperationService<StoreValidationRuleData> {
    @Override
    protected void onOperating(StoreValidationRuleData ruleData, IOperationCollection<StoreValidationRuleData, Boolean> operationCollection) {
       operationCollection
                .add(ValidateStoreOrderOperation.class)
                .add(ValidateStoreOpeningTimeOperation.class);
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
