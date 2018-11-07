package com.sztouyun.advertisingsystem.service.advertisement;

import com.sztouyun.advertisingsystem.model.advertisement.Advertisement;
import com.sztouyun.advertisingsystem.model.advertisement.AdvertisementOperationLog;
import com.sztouyun.advertisingsystem.model.advertisement.AdvertisementStatusEnum;
import com.sztouyun.advertisingsystem.model.advertisement.QAdvertisement;
import com.sztouyun.advertisingsystem.repository.advertisement.AdvertisementOperationLogRepository;
import com.sztouyun.advertisingsystem.repository.advertisement.AdvertisementRepository;
import com.sztouyun.advertisingsystem.service.BaseService;
import com.sztouyun.advertisingsystem.service.message.event.AdvertisementOperationEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class AdvertisementOperationLogService extends BaseService {
    @Autowired
    private AdvertisementOperationLogRepository advertisementOperationLogRepository;
    @Autowired
    private AdvertisementRepository advertisementRepository;
    private final QAdvertisement qAdvertisement = QAdvertisement.advertisement;

    public void saveAdvertisementOperationLog(AdvertisementOperationLog advertisementOperationLog) {
        AdvertisementOperationLog log = advertisementOperationLogRepository.save(advertisementOperationLog);
        Advertisement advertisement = advertisementRepository.findOne(log.getAdvertisementId());
        log.setAutoTakeOff(advertisementOperationLog.isAutoTakeOff());
        log.setChangeByContract(advertisementOperationLog.getChangeByContract());
        log.setAffectOtherAdvertisements(advertisementRepository.exists(
                qAdvertisement.advertisementStatus.notIn(Arrays.asList(AdvertisementStatusEnum.Finished.getValue(),AdvertisementStatusEnum.TakeOff.getValue()))
                        .and(qAdvertisement.contractId.eq(advertisement.getContractId())).and(qAdvertisement.id.ne(advertisementOperationLog.getAdvertisementId()))));
        publishEvent(new AdvertisementOperationEvent(log));
    }
}
