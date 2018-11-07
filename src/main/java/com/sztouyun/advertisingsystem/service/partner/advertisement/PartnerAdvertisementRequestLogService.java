package com.sztouyun.advertisingsystem.service.partner.advertisement;


import com.sztouyun.advertisingsystem.model.monitor.PartnerDailyStoreMonitorStatistic;
import com.sztouyun.advertisingsystem.service.BaseService;
import com.sztouyun.advertisingsystem.utils.ThreadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;


@Service
public class PartnerAdvertisementRequestLogService extends BaseService {
    private static ConcurrentLinkedQueue<PartnerDailyStoreMonitorStatistic> requestLogQueue = new ConcurrentLinkedQueue<>();

    @Autowired
    private PartnerRequestLogService partnerRequestLogService;

    public void sendPartnerAdvertisementRequestLog(PartnerDailyStoreMonitorStatistic requestLog) {
        try {
            requestLogQueue.offer(requestLog);
        } catch (Exception e) {
            logger.error("发送合作方广告请求日志到队列失败",e);
        }
    }

    public void savePartnerRequestLog() {
        List<PartnerDailyStoreMonitorStatistic> partnerAdvertisementRequestLogs = new ArrayList<>();
        PartnerDailyStoreMonitorStatistic requestLog;
        while (true) {
            try {
                requestLog = requestLogQueue.poll();
                if(requestLog == null){
                    if(!CollectionUtils.isEmpty(partnerAdvertisementRequestLogs)){
                        partnerRequestLogService.savePartnerAdvertisementRequestLog(partnerAdvertisementRequestLogs);
                        partnerAdvertisementRequestLogs = new ArrayList<>();
                    }
                    ThreadUtil.sleep(500);
                    continue;
                }
                partnerAdvertisementRequestLogs.add(requestLog);
                if(partnerAdvertisementRequestLogs.size() == 200){
                    partnerRequestLogService.savePartnerAdvertisementRequestLog(partnerAdvertisementRequestLogs);
                    partnerAdvertisementRequestLogs = new ArrayList<>();
                }
            }catch (Exception e){
                logger.error("保存合作方广告请求日志失败",e);
            }
        }
    }
}
