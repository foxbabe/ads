package com.sztouyun.advertisingsystem.repository.contract;

import com.sztouyun.advertisingsystem.model.contract.ContractAdvertisementPositionConfig;
import com.sztouyun.advertisingsystem.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContractAdvertisementPositionConfigRepository extends BaseRepository<ContractAdvertisementPositionConfig> {
    List<ContractAdvertisementPositionConfig> findAllByContractId(String contractId);
    @Modifying
    @Query(value = "DELETE FROM ContractAdvertisementPositionConfig WHERE contractId = :contractId")
    void deletePositionConfigByContractId(@Param("contractId")String contractId);
}
