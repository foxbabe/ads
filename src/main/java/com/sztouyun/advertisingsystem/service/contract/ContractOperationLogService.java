package com.sztouyun.advertisingsystem.service.contract;

import com.querydsl.core.BooleanBuilder;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.viewmodel.contract.ContractOperationStatusEnum;
import com.sztouyun.advertisingsystem.model.contract.ContractOperationLog;
import com.sztouyun.advertisingsystem.model.contract.QContractOperationLog;
import com.sztouyun.advertisingsystem.repository.contract.ContractOperationLogRepository;
import com.sztouyun.advertisingsystem.repository.contract.ContractRepository;
import com.sztouyun.advertisingsystem.service.BaseService;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import com.sztouyun.advertisingsystem.viewmodel.common.MyPageRequest;
import com.sztouyun.advertisingsystem.viewmodel.contract.ContractOperationLogRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Created by wenfeng on 2017/11/22.
 */
@Service
public class ContractOperationLogService extends BaseService {
    @Autowired
    private ContractOperationLogRepository contractOperationLogRepository;
    @Autowired
    private ContractRepository contractRepository;

    private final QContractOperationLog qContractOperationLog=QContractOperationLog.contractOperationLog;
    public Page<ContractOperationLog> getOperationLogsByContract(ContractOperationLogRequest request){
        String contractId=request.getId();
        if(!contractRepository.exists(contractId))
            throw new BusinessException("合同ID无效");
        Pageable pageable = new MyPageRequest(request.getPageIndex(), request.getPageSize());
        BooleanBuilder predicate = new BooleanBuilder();
        predicate.and(qContractOperationLog.contractId.eq(contractId));
        if(request.getOperationStatus()!=null){
            ContractOperationStatusEnum statusEnum=EnumUtils.toEnum(request.getOperationStatus(),ContractOperationStatusEnum.class);
            if(statusEnum!=null && !statusEnum.equals(ContractOperationStatusEnum.All)){
                predicate.and(qContractOperationLog.operation.eq(statusEnum.getOperation())).and(qContractOperationLog.successed.eq(statusEnum.getSucceeded()==null?true:statusEnum.getSucceeded()));
            }
        }
        if(request.getStartTime()!=null){
            predicate.and(qContractOperationLog.createdTime.goe(request.getStartTime()));
        }
        if(request.getEndTime()!=null){
            predicate.and(qContractOperationLog.createdTime.loe(request.getEndTime()));
        }
        return contractOperationLogRepository.findAllAuthorized(predicate, pageable);
    }
}
