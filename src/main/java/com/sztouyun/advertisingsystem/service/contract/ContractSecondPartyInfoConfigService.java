package com.sztouyun.advertisingsystem.service.contract;

import com.querydsl.core.BooleanBuilder;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.contract.ContractSecondPartyInfoConfig;
import com.sztouyun.advertisingsystem.repository.contract.ContractSecondPartyInfoConfigRepository;
import com.sztouyun.advertisingsystem.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;

@Service
public class ContractSecondPartyInfoConfigService extends BaseService {

    @Autowired
    private ContractSecondPartyInfoConfigRepository configRepository;


    @Transactional
    public String updateContractSecondPartyInfoConfig(ContractSecondPartyInfoConfig contractSecondPartyInfoConfig) {
        if(contractSecondPartyInfoConfig == null)
            throw new BusinessException("乙方合同信息不能为空");

        contractSecondPartyInfoConfig.setUpdatedTime(new Date());
        configRepository.save(contractSecondPartyInfoConfig);
        return contractSecondPartyInfoConfig.getId();
    }

    public ContractSecondPartyInfoConfig getContractSecondPartyInfoConfig() {
        return configRepository.findOne(new BooleanBuilder());
    }
}
