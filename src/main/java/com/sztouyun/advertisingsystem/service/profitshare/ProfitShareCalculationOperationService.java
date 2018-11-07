package com.sztouyun.advertisingsystem.service.profitshare;

import com.sztouyun.advertisingsystem.common.operation.IOperationCollection;
import com.sztouyun.advertisingsystem.service.profitshare.base.BaseProfitShareOperationService;
import com.sztouyun.advertisingsystem.service.profitshare.operations.CalculateStoreAdvertisementProfitShareOperation;
import com.sztouyun.advertisingsystem.service.profitshare.operations.data.ProfitShareCalculationData;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public  class ProfitShareCalculationOperationService extends BaseProfitShareOperationService<ProfitShareCalculationData,Double> {
    @Override
    protected void onOperating(ProfitShareCalculationData profitShareRuleData, IOperationCollection<ProfitShareCalculationData, Double> operationCollection) {
        operationCollection.add(CalculateStoreAdvertisementProfitShareOperation.class);
    }

    @Override
    protected Double getResult(List<Double> resultList) {
        double total = 0;
        for (Double result :resultList){
            total += result;
        }
        return total;
    }
}
