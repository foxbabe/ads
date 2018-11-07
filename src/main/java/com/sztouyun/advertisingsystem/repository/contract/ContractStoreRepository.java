package com.sztouyun.advertisingsystem.repository.contract;

import com.sztouyun.advertisingsystem.model.contract.ContractStore;
import com.sztouyun.advertisingsystem.repository.BaseRepository;

public interface ContractStoreRepository extends BaseRepository<ContractStore> {
    void deleteByContractIdAndStoreType(String contractId, int storeType);

    void deleteByContractIdAndStoreId(String contractId, String storeId);

    void deleteByContractId(String contractId);
}