package com.sztouyun.advertisingsystem.service.partner.advertisement;

import com.sztouyun.advertisingsystem.common.mq.BaseScheduledReceiveMessagesService;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.mongodb.PartnerAdvertisementDeliveryRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReceivePartnerAdvertisementDeliveryRecordService extends BaseScheduledReceiveMessagesService<PartnerAdvertisementDeliveryRecord> {
    @Value("${partner.advertisement.delivery.record.queue.name}")
    private String queueName;

    @Autowired
    private PartnerAdvertisementDeliveryRecordService partnerAdvertisementDeliveryRecordService;

    @Scheduled(cron = "${partner.advertisement.delivery.record.save.time}")
    public void receiveMessages1(){
        receiveMessages(queueName,400,PartnerAdvertisementDeliveryRecord.class, 1);
    }

    protected boolean handleMessages(List<PartnerAdvertisementDeliveryRecord> messages) {
        try {
            partnerAdvertisementDeliveryRecordService.savePartnerAdvertisementDeliveryRecord(messages);
        } catch (BusinessException e) {
            logger.error("保存第三方广告投放记录失败", e);
        }
        return true;
    }
}
