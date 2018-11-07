package com.sztouyun.advertisingsystem.service.partner.advertisement;

import com.sztouyun.advertisingsystem.common.mq.BaseScheduledReceiveMessagesService;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.viewmodel.partner.PartnerAdvertisementDeliveryRecordLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReceivePartnerAdvertisementDeliveryRecordLogService extends BaseScheduledReceiveMessagesService<PartnerAdvertisementDeliveryRecordLog> {
    @Value("${partner.delivery.log.queue.name}")
    private String queueName;

    @Autowired
    private PartnerAdvertisementDeliveryRecordService partnerAdvertisementDeliveryRecordService;

    @Scheduled(cron = "${partner.delivery.log.save.time}")
    public void receiveMessages(){
        receiveMessages(queueName,300,PartnerAdvertisementDeliveryRecordLog.class,1);
    }

    protected boolean handleMessages(List<PartnerAdvertisementDeliveryRecordLog> messages) {
        try {
            List<PartnerAdvertisementDeliveryRecordLog> startDeliveryLogs = new ArrayList();
            List<PartnerAdvertisementDeliveryRecordLog> endDeliveryLogs = new ArrayList();

            messages.stream().forEach(log -> {
                    if(log.getAction().equals(PartnerAdvertisementTrackEnum.StartPlay.getValue())) {
                        startDeliveryLogs.add(log);
                    } else {
                        endDeliveryLogs.add(log);
                    }
            });
            if (!CollectionUtils.isEmpty(startDeliveryLogs)) {
                partnerAdvertisementDeliveryRecordService.savePartnerAdvertisementStartDeliveryRecordLog(startDeliveryLogs);
            }
            if (!CollectionUtils.isEmpty(endDeliveryLogs)) {
                partnerAdvertisementDeliveryRecordService.savePartnerAdvertisementEndDeliveryRecordLog(endDeliveryLogs);
            }
        } catch (BusinessException e) {
            logger.error("保存第三方投放日志失败", e);
        }
        return true;
    }
}
