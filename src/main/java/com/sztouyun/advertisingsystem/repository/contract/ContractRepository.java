package com.sztouyun.advertisingsystem.repository.contract;

import com.querydsl.core.BooleanBuilder;
import com.sztouyun.advertisingsystem.model.contract.Contract;
import com.sztouyun.advertisingsystem.model.contract.QContract;
import com.sztouyun.advertisingsystem.model.store.StoreInfo;
import com.sztouyun.advertisingsystem.repository.BaseRepository;
import com.sztouyun.advertisingsystem.service.account.AuthenticationService;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContractRepository extends BaseRepository<Contract> {
    @Query(value = "SELECT new StoreInfo(storeInfo.id,storeInfo.storeNo,storeInfo.storeName,contractStore.storeType,storeInfo.storeAddress,storeInfo.cityId) FROM ContractStore  contractStore , StoreInfo storeInfo where contractStore.contractId=?1 and contractStore.storeId=storeInfo.id  ")
    List<StoreInfo> getStoreInfoListByContractId(String contractId);

    @Modifying
    @Query(value = "UPDATE Contract set ownerId = :ownerId WHERE customerId = :customerId AND ownerId = :oldCustomerOwnerId and contractStatus IN (1, 2, 3)")
    void updateUnsignedContractOwner(@Param("ownerId") String ownerId, @Param("customerId") String customerId, @Param("oldCustomerOwnerId") String oldCustomerOwnerId);

    @Override
    default BooleanBuilder getAuthorizationFilter() {
        return AuthenticationService.getUserAuthenticationFilter(QContract.contract.ownerId);
    }

    boolean existsByOwnerId(String ownerId);

    boolean existsByCreatorId(String userId);
}
