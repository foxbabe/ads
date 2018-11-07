package com.sztouyun.advertisingsystem.repository.contract;

import com.sztouyun.advertisingsystem.model.contract.ContractOperationLog;
import com.sztouyun.advertisingsystem.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.Date;

public interface ContractOperationLogRepository extends BaseRepository<ContractOperationLog> {
    @Query(value = "select created_time from contract_operation_log  where contract_id=:contractId and successed=1 and operation=2 order by created_time desc limit 0,1",nativeQuery = true)
    Date findByLastestPassAuditingTime(@Param("contractId") String contractId);
}
