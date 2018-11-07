package com.sztouyun.advertisingsystem.service.advertisement.operations;

import com.sztouyun.advertisingsystem.common.operation.IActionOperation;
import com.sztouyun.advertisingsystem.model.advertisement.Advertisement;
import com.sztouyun.advertisingsystem.model.monitor.AdvertisementMonitorStatistic;
import com.sztouyun.advertisingsystem.repository.monitor.AdvertisementMonitorStatisticRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UpdateAdvertisementMonitorEndTimeOperation implements IActionOperation<Advertisement> {
    @Autowired
    private AdvertisementMonitorStatisticRepository advertisementMonitorStatisticRepository;
    @Override
    public void operateAction(Advertisement advertisement) {
        AdvertisementMonitorStatistic advertisementMonitorStatistic=advertisementMonitorStatisticRepository.findOne(advertisement.getId());
        if(advertisementMonitorStatistic!=null){
            advertisementMonitorStatistic.setEndTime(new Date());
            advertisementMonitorStatisticRepository.save(advertisementMonitorStatistic);
        }
    }
}
