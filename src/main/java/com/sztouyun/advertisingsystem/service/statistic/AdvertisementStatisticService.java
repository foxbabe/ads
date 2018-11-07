package com.sztouyun.advertisingsystem.service.statistic;

import com.querydsl.core.types.Projections;
import com.sztouyun.advertisingsystem.model.advertisement.AdvertisementStatusEnum;
import com.sztouyun.advertisingsystem.model.advertisement.QAdvertisement;
import com.sztouyun.advertisingsystem.viewmodel.index.DistributionStatisticDto;
import com.sztouyun.advertisingsystem.viewmodel.statistic.SummaryStatisticTypeEnum;
import com.sztouyun.advertisingsystem.viewmodel.statistic.SummaryStatistic;
import com.sztouyun.advertisingsystem.repository.advertisement.AdvertisementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdvertisementStatisticService {

    @Autowired
    private AdvertisementRepository advertisementRepository;

    private final QAdvertisement qAdvertisement = QAdvertisement.advertisement;

    public SummaryStatistic getTotalDeliveryAdvertisement() {
        long total = advertisementRepository.countAuthorized(qAdvertisement.advertisementStatus.eq(AdvertisementStatusEnum.Delivering.getValue()));
        SummaryStatistic summaryStatistic = new SummaryStatistic();
        summaryStatistic.setTotal(total);
        summaryStatistic.setStatisticType(SummaryStatisticTypeEnum.DeliveringAdvertisement.getValue());
        return summaryStatistic;
    }

    public List<DistributionStatisticDto> getAdvertisementStatusStatistics() {
        return advertisementRepository.findAllAuthorized(q -> q
                .select(Projections.bean(DistributionStatisticDto.class,
                        qAdvertisement.advertisementStatus.as("keyValue"),
                        qAdvertisement.id.count().as("value")))
                .from(qAdvertisement)
                .where(qAdvertisement.advertisementStatus.ne(AdvertisementStatusEnum.PendingCommit.getValue()))
                .groupBy(qAdvertisement.advertisementStatus));
    }

    public Long getAdvertisementCount(){
        return advertisementRepository.countAuthorized(q->q.select(qAdvertisement)
                .from(qAdvertisement).where(qAdvertisement.advertisementStatus.ne(AdvertisementStatusEnum.PendingCommit.getValue())));
    }
}
