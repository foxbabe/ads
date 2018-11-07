package com.sztouyun.advertisingsystem.service.contract.operations;

import com.sztouyun.advertisingsystem.common.operation.IActionOperation;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.contract.Contract;
import com.sztouyun.advertisingsystem.model.contract.ContractExtension;
import com.sztouyun.advertisingsystem.model.contract.ContractStore;
import com.sztouyun.advertisingsystem.model.contract.StoreTypeEnum;
import com.sztouyun.advertisingsystem.service.contract.ContractService;
import org.apache.calcite.linq4j.Enumerable;
import org.apache.calcite.linq4j.Linq4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ValidateContractStoreCountOperation implements IActionOperation<Contract> {
    @Autowired
    private ContractService contractService;

    @Override
    public void operateAction(Contract contract) {
        ContractExtension contractExtension = contract.getContractExtension();
        if (contractExtension == null)
            throw new BusinessException("合同信息不完整！");

        if (contract.getContractExtension().getHasCashRegister()) {
            List<ContractStore> contractStoreList = contractService.getContractStores(contract.getId());
            if(contractExtension.getTotalStoreCount() == 0 || contractStoreList == null || contractStoreList.isEmpty())
                throw new BusinessException("请最少选择一家门店！");

            Enumerable<ContractStore> contractStores = Linq4j.asEnumerable(contractStoreList);
            if (contractExtension.getStoreACount() != contractStores.count(c -> c.getStoreType().equals(StoreTypeEnum.A.getValue()))
                    || contractExtension.getStoreBCount() != contractStores.count(c -> c.getStoreType().equals(StoreTypeEnum.B.getValue()))
                    || contractExtension.getStoreCCount() != contractStores.count(c -> c.getStoreType().equals(StoreTypeEnum.C.getValue()))
                    || contractExtension.getStoreDCount() != contractStores.count(c -> c.getStoreType().equals(StoreTypeEnum.D.getValue())))
                throw new BusinessException("请先完成门店选择！");
        }
    }
}
