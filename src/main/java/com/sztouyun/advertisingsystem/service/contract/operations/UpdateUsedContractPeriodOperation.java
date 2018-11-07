package com.sztouyun.advertisingsystem.service.contract.operations;

import com.sztouyun.advertisingsystem.common.operation.IActionOperation;
import com.sztouyun.advertisingsystem.model.advertisement.AdvertisementStatusEnum;
import com.sztouyun.advertisingsystem.model.advertisement.QAdvertisement;
import com.sztouyun.advertisingsystem.model.contract.Contract;
import com.sztouyun.advertisingsystem.repository.advertisement.AdvertisementRepository;
import com.sztouyun.advertisingsystem.repository.contract.ContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdateUsedContractPeriodOperation implements IActionOperation<Contract> {
    @Autowired
    private AdvertisementRepository advertisementRepository;

    @Autowired
    private ContractRepository contractRepository;

    private final QAdvertisement qAdvertisement =QAdvertisement.advertisement;

    @Override
    public void operateAction(Contract contract) {
        //更新合同已投放广告时间
      Integer usedContractPeriod= advertisementRepository.findOne(q->q.select(qAdvertisement.effectivePeriod.sum())
              .from(qAdvertisement)
              .where(qAdvertisement.contractId.eq(contract.getId())
              .and(qAdvertisement.advertisementStatus.eq(AdvertisementStatusEnum.TakeOff.getValue()).or(qAdvertisement.advertisementStatus.eq(AdvertisementStatusEnum.Finished.getValue())))));
      if(usedContractPeriod != null){
          contract.setUsedContractPeriod(usedContractPeriod);
          contractRepository.save(contract);
      }
    }
}
