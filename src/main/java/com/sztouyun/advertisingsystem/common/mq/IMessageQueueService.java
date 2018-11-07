package com.sztouyun.advertisingsystem.common.mq;


import java.util.List;

public interface IMessageQueueService {
    void sendMessage(String queueName, MessageInfo messageInfo,int index) throws Exception;
    List<MessageInfo> receiveMessages(String queueName, int count,int index) throws Exception;
    void  completeAsync(String queueName, Object lockToken,int index);
}
