package com.sztouyun.advertisingsystem.service.contract.operations;

import com.sztouyun.advertisingsystem.common.operation.IActionOperation;
import com.sztouyun.advertisingsystem.model.advertisement.Advertisement;
import com.sztouyun.advertisingsystem.model.advertisement.AdvertisementStatusEnum;
import com.sztouyun.advertisingsystem.model.advertisement.QAdvertisement;
import com.sztouyun.advertisingsystem.model.contract.ContractOperationEnum;
import com.sztouyun.advertisingsystem.model.contract.ContractOperationLog;
import com.sztouyun.advertisingsystem.repository.advertisement.AdvertisementRepository;
import com.sztouyun.advertisingsystem.repository.contract.ContractOperationLogRepository;
import com.sztouyun.advertisingsystem.service.BaseService;
import com.sztouyun.advertisingsystem.service.contract.ContractService;
import com.sztouyun.advertisingsystem.service.message.event.ContractOperationEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class SaveContractOperationLogOperation extends BaseService implements IActionOperation<ContractOperationLog> {
    @Autowired
    private ContractOperationLogRepository contractOperationLogRepository;

    @Autowired
    private AdvertisementRepository advertisementRepository;

    private final QAdvertisement qAdvertisement = QAdvertisement.advertisement;

    @Override
    public void operateAction(ContractOperationLog contractOperationLog) {
        if(contractOperationLog.getContractOperationEnum().equals(ContractOperationEnum.Finish)){
            List<Integer> finishedAdvertisementStatus = Arrays.asList(AdvertisementStatusEnum.Finished.getValue(),AdvertisementStatusEnum.TakeOff.getValue());
            Iterable<Advertisement> unfinishedAdvertisements = advertisementRepository.findAll(
                    qAdvertisement.advertisementStatus.notIn(finishedAdvertisementStatus)
                            .and(qAdvertisement.contractId.eq(contractOperationLog.getContractId())));
            contractOperationLog.setUnfinishedAdvertisements(unfinishedAdvertisements);
        }
        contractOperationLogRepository.save(contractOperationLog);
        publishEvent(new ContractOperationEvent(contractOperationLog));
    }
}
