package com.sztouyun.advertisingsystem.repository.contract;

import com.sztouyun.advertisingsystem.model.contract.Preview;
import com.sztouyun.advertisingsystem.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PreviewRepository extends BaseRepository<Preview> {
    @Query(value = "select * from  preview  where contract_id=:contractId order by updated_time desc limit 0,1",nativeQuery=true)
    Preview findByContractId(@Param("contractId") String contractId);
}
