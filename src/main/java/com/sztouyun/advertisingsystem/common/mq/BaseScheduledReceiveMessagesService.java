package com.sztouyun.advertisingsystem.common.mq;

import com.sztouyun.advertisingsystem.service.BaseService;
import com.sztouyun.advertisingsystem.service.account.AuthenticationService;
import com.sztouyun.advertisingsystem.utils.ObjectMapperUtils;
import org.apache.calcite.linq4j.Linq4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.List;


public abstract class BaseScheduledReceiveMessagesService<T> extends BaseService {
    @Autowired
    private IMessageQueueService messageQueueService;

    protected void receiveMessages(String queueName,int batchCount,Class<T> messageClass,int index){
        AuthenticationService.setAdminLogin();
        List<MessageInfo> messages =null;
        do {
            try {
                messages = messageQueueService.receiveMessages(queueName,batchCount,index);
                if(messages == null || messages.isEmpty())
                    break;

                boolean result = handleMessages(convertMessages(messages,messageClass));
                if (result) {
                    messages.stream().forEach(message-> messageQueueService.completeAsync(queueName, message.getLockToken(),index));
                }
            }
            catch (Exception e){
                logger.error("消息获取失败",e);
            }
        }while (!CollectionUtils.isEmpty(messages));
    }

    protected List<T> convertMessages(List<MessageInfo> messages,Class<T> messageClass){
        return Linq4j.asEnumerable(messages).select(m-> ObjectMapperUtils.jsonToObject(m.getContent(),messageClass)).toList();
    }

    protected abstract boolean handleMessages(List<T> messages);
}
