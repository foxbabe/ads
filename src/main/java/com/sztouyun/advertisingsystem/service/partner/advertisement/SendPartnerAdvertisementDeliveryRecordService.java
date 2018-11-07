package com.sztouyun.advertisingsystem.service.partner.advertisement;


import com.sztouyun.advertisingsystem.common.mq.IMessageQueueService;
import com.sztouyun.advertisingsystem.common.mq.MessageInfo;
import com.sztouyun.advertisingsystem.model.mongodb.PartnerAdvertisementDeliveryRecord;
import com.sztouyun.advertisingsystem.service.BaseService;
import com.sztouyun.advertisingsystem.utils.MathUtil;
import com.sztouyun.advertisingsystem.utils.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;


@Service
public class SendPartnerAdvertisementDeliveryRecordService extends BaseService {
    @Value("${partner.advertisement.delivery.record.queue.name}")
    private String queueName;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private IMessageQueueService messageQueueService;

    public void sendPartnerAdvertisementInfo(PartnerAdvertisementDeliveryRecord record) {
        try {
            //保存三方广告投放记录
            mongoTemplate.insert(record);
            messageQueueService.sendMessage(queueName,new MessageInfo(ObjectMapperUtils.toJsonString(record), null), MathUtil.randomInt(50));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("发送合作方广告门店投放日志到消息队列失败",e);
        }
    }
}
