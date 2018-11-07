package com.sztouyun.advertisingsystem.service.contract;

import com.sztouyun.advertisingsystem.model.contract.QContractStore;
import com.sztouyun.advertisingsystem.repository.contract.ContractStoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContractStoreService {

    @Autowired
    private ContractStoreRepository contractStoreRepository;

    private final static QContractStore qContractStore = QContractStore.contractStore;
}
