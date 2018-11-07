package com.sztouyun.advertisingsystem.service.contract;

import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.contract.ContractTemplate;
import com.sztouyun.advertisingsystem.model.contract.ContractTemplateTypeEnum;
import com.sztouyun.advertisingsystem.model.contract.QContractTemplate;
import com.sztouyun.advertisingsystem.repository.contract.ContractTemplateRepository;
import com.sztouyun.advertisingsystem.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ContractTemplateService extends BaseService{

    @Autowired
    private ContractTemplateRepository contractTemplateRepository;

    private final QContractTemplate qContractTemplate = QContractTemplate.contractTemplate;

    public ContractTemplate getLatestContractTemplateByType(Integer templateType) {
        return contractTemplateRepository.findOne(query -> query.select(qContractTemplate).from(qContractTemplate).where(qContractTemplate.templateType.eq(templateType)).orderBy(qContractTemplate.updatedTime.desc()).limit(1));
    }

    public List<ContractTemplate> getAllTypeTemplates() {
        List<ContractTemplate> contractTemplateList = new ArrayList<>();

        ContractTemplate requireFeesContractTemplate = getLatestContractTemplateByType(ContractTemplateTypeEnum.REQUIRE_FEES.getValue());
        ContractTemplate freeContractTemplate = getLatestContractTemplateByType(ContractTemplateTypeEnum.FREE.getValue());

        if (requireFeesContractTemplate == null || freeContractTemplate == null)
            throw new BusinessException("合同模板数据不完整");

        contractTemplateList.add(requireFeesContractTemplate);
        contractTemplateList.add(freeContractTemplate);

        return contractTemplateList;
    }

    public ContractTemplate getContractTemplate(String templateId){
        return contractTemplateRepository.findOne(templateId);
    }

}
