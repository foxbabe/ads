package com.sztouyun.advertisingsystem.common.mq.impl;

import com.microsoft.azure.servicebus.*;
import com.microsoft.azure.servicebus.primitives.ConnectionStringBuilder;
import com.microsoft.azure.servicebus.primitives.ServiceBusException;
import com.sztouyun.advertisingsystem.common.mq.IMessageQueueService;
import com.sztouyun.advertisingsystem.common.mq.MessageInfo;
import com.sztouyun.advertisingsystem.service.BaseService;
import lombok.experimental.var;
import org.apache.calcite.linq4j.Linq4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
public class ServiceBusService extends BaseService implements IMessageQueueService {
    private final Map<String,QueueClient> sendClientMap = new ConcurrentHashMap<>();
    private final Map<String,IMessageReceiver> messageReceiverMap = new ConcurrentHashMap<>();

    @Value("${azure.servicebus.connection-string}")
    private String connectionString;

    @Override
    public void sendMessage(String queueName, MessageInfo messageInfo,int index) throws ServiceBusException, InterruptedException {
        QueueClient sendClient = getSendQueueClient(queueName,index);
        Message message = new Message(messageInfo.getContent().getBytes(UTF_8));
        message.setContentType("application/json");
        message.setLabel(messageInfo.getLabel());
        if(messageInfo.getDuration() !=null){
            message.setTimeToLive(messageInfo.getDuration());
        }
        sendClient.sendAsync(message);
    }

    @Override
    public List<MessageInfo> receiveMessages(String queueName , int count,int index) throws ServiceBusException, InterruptedException {
        IMessageReceiver receiver = getMessageReceiver(queueName,index);
        Collection<IMessage> messages = receiver.receiveBatch(count,Duration.ofSeconds(5));
        if(messages == null || messages.isEmpty())
            return null;
        return Linq4j.asEnumerable(messages).select(message ->
                new MessageInfo( new String(message.getBody(), UTF_8),message.getLabel(),message.getLockToken())).toList();
    }

    @Override
    public void completeAsync(String queueName, Object lockToken,int index){
        try {
            IMessageReceiver receiver = getMessageReceiver(queueName,index);
            receiver.completeAsync((UUID)lockToken);
        } catch (Exception e) {
        }
    }

    private IMessageReceiver getMessageReceiver(String queueName,int index) throws ServiceBusException, InterruptedException {
        var key =queueName+index;
        IMessageReceiver receiver = messageReceiverMap.get(key);
        if(receiver != null)
            return receiver;
        receiver = ClientFactory.createMessageReceiverFromConnectionStringBuilder(
                new ConnectionStringBuilder(connectionString, queueName), ReceiveMode.PEEKLOCK);
        receiver.setPrefetchCount(200);
        logger.info("开始监听消息队列："+key);
        messageReceiverMap.put(key,receiver);
        return receiver;
    }

    private QueueClient getSendQueueClient(String queueName,int index) throws ServiceBusException, InterruptedException {
        var key =queueName+index;
        QueueClient sendClient = sendClientMap.get(key);
        if(sendClient !=null)
            return sendClient;
        sendClient = new QueueClient(new ConnectionStringBuilder(connectionString, queueName), ReceiveMode.PEEKLOCK);
        logger.info("创建发送消息队列："+key);
        sendClientMap.put(key,sendClient);
        return sendClient;
    }
}

