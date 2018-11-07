package com.sztouyun.advertisingsystem.service.contract.operations;

import com.sztouyun.advertisingsystem.common.operation.IActionOperation;
import com.sztouyun.advertisingsystem.model.advertisement.*;
import com.sztouyun.advertisingsystem.model.contract.Contract;
import com.sztouyun.advertisingsystem.model.contract.ContractStatusEnum;
import com.sztouyun.advertisingsystem.repository.advertisement.AdvertisementRepository;
import com.sztouyun.advertisingsystem.service.advertisement.AdvertisementOperationService;
import com.sztouyun.advertisingsystem.service.advertisement.operations.AdvertisementContractActionOperation;
import com.sztouyun.advertisingsystem.service.advertisement.operations.ValidateAdvertisementStatusOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class TakeOffContractAdvertisementOperation implements IActionOperation<Contract> {
    @Autowired
    private AdvertisementRepository advertisementRepository;

    @Autowired
    private AdvertisementOperationService advertisementOperationService;

    private final QAdvertisement qAdvertisement = QAdvertisement.advertisement;

    @Override
    public void operateAction(Contract contract) {
        List<Integer> finishedAdvertisementStatus = Arrays.asList(AdvertisementStatusEnum.Finished.getValue(),AdvertisementStatusEnum.TakeOff.getValue());
        Iterable<Advertisement> unfinishedAdvertisements = advertisementRepository.findAll(
                qAdvertisement.advertisementStatus.notIn(finishedAdvertisementStatus)
                .and(qAdvertisement.contractId.eq(contract.getId())));
        //下架合同下面所有未完成的广告
        unfinishedAdvertisements.forEach(advertisement -> {
            String remark = getAdvertisementTakeOffRemark(advertisement,contract);
            AdvertisementOperationLog advertisementOperationLog = new AdvertisementOperationLog(advertisement.getId(), AdvertisementOperationEnum.Finish.getValue(),false,remark);
            advertisementOperationLog.setAdvertisement(advertisement);

            advertisementOperationLog.setChangeByContract(true);
            //强制下架排除广告状态验证和广告下架的合同回调
            advertisementOperationService.operate(advertisementOperationLog,ValidateAdvertisementStatusOperation.class, AdvertisementContractActionOperation.class);
        });
    }

    private String getAdvertisementTakeOffRemark(Advertisement advertisement,Contract contract){
        if(contract.getContractStatusEnum().equals(ContractStatusEnum.Finished))
            return "“"+contract.getContractName()+"”"+"合同执行完成，无法再进行投放，导致广告自动下架";
        if(advertisement.getAdvertisementStatusEnum().equals(AdvertisementStatusEnum.Delivering))
            return "“"+contract.getContractName()+"”"+"合同意外终止导致广告自动下架";
        return "“"+contract.getContractName()+"”"+"合同意外终止，关联的未投放状态的广告记录自动下架";
    }
}
