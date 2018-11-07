package com.sztouyun.advertisingsystem.service.partner.advertisement;

import com.sztouyun.advertisingsystem.mapper.PartnerAdvertisementMapper;
import com.sztouyun.advertisingsystem.model.monitor.PartnerDailyStoreMonitorStatistic;
import com.sztouyun.advertisingsystem.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PartnerRequestLogService extends BaseService {
    @Autowired
    private PartnerAdvertisementMapper partnerAdvertisementMapper;

    public void savePartnerAdvertisementRequestLog(List<PartnerDailyStoreMonitorStatistic> requestLogList) {
        partnerAdvertisementMapper.updatePartnerRequestStatistic(requestLogList);
    }
}
