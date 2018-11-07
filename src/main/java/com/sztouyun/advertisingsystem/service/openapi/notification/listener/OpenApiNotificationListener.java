package com.sztouyun.advertisingsystem.service.openapi.notification.listener;

import com.sztouyun.advertisingsystem.common.mq.IMessageQueueService;
import com.sztouyun.advertisingsystem.common.mq.MessageInfo;
import com.sztouyun.advertisingsystem.model.partner.QCooperationPartner;
import com.sztouyun.advertisingsystem.repository.partner.CooperationPartnerRepository;
import com.sztouyun.advertisingsystem.service.openapi.notification.OpenApiNotification;
import com.sztouyun.advertisingsystem.service.openapi.notification.data.base.OpenApiNotificationData;
import com.sztouyun.advertisingsystem.service.partner.CooperationPartnerService;
import com.sztouyun.advertisingsystem.utils.ObjectMapperUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OpenApiNotificationListener implements ApplicationListener<OpenApiNotification> {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${open.api.notification.queue.name}")
    private String queueName;

    @Autowired
    private CooperationPartnerRepository partnerRepository;
    @Autowired
    private CooperationPartnerService cooperationPartnerService;
    @Autowired
    private IMessageQueueService messageQueueService;
    private final QCooperationPartner qCooperationPartner = QCooperationPartner.cooperationPartner;

    @Override
    @Async
    public void onApplicationEvent(OpenApiNotification openApiNotification) {
        OpenApiNotificationData notificationData =openApiNotification.getNotificationData();
        try {
            //有合作方ID就通知对应的合作方
            if(cooperationPartnerService.partnerIsEnabled(notificationData.getPartnerId())){
                sendMessage(notificationData);
            }else {
                //无合作方ID就通知所有未禁用的合作方
                List<String> partnerIds = partnerRepository.findAll(q->q.select(qCooperationPartner.id).from(qCooperationPartner).where(qCooperationPartner.disabled.isFalse()));
                for (String partnerId : partnerIds){
                    notificationData.setPartnerId(partnerId);
                    sendMessage(notificationData);
                }
            }
        }catch (Exception e){
            logger.error("发送消息到消息队列失败",e);
        }
    }

    private void sendMessage(OpenApiNotificationData notificationData) {
        try {
            messageQueueService.sendMessage(queueName,new MessageInfo(ObjectMapperUtils.toJsonString(notificationData),notificationData.getNotificationType().getValue().toString()), 1);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("发送OpenApiNotificationData到消息队列失败",e);
        }
    }
}
