package com.sztouyun.advertisingsystem.common.sms;

import com.aliyun.mns.client.CloudAccount;
import com.aliyun.mns.client.CloudTopic;
import com.aliyun.mns.client.MNSClient;
import com.aliyun.mns.common.ServiceException;
import com.aliyun.mns.model.BatchSmsAttributes;
import com.aliyun.mns.model.MessageAttributes;
import com.aliyun.mns.model.RawTopicMessage;
import com.aliyun.mns.model.TopicMessage;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.service.BaseService;
import com.sztouyun.advertisingsystem.utils.StringUtils;
import com.sztouyun.advertisingsystem.utils.ValidationUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AliyunSmsService extends BaseService implements ISmsService {
    @Value("${shop.aliyun.mns.access-key-id}")
    private String accessId;

    @Value("${shop.aliyun.mns.access-key-secret}")
    private String accessSecret;

    @Value("${shop.aliyun.mns.mns-endpoint}")
    private String endpoint;

    @Value("${shop.aliyun.mns.topic}")
    private String smsTopic;

    @Value("${shop.aliyun.mns.sign-name}")
    private String signName;

    @Override
    public void sendMessage(SmsMessage smsMessage) {
        if(org.springframework.util.StringUtils.isEmpty(smsMessage.getTemplateId()))
            throw new BusinessException("短信模板不能为空");
        if(!ValidationUtils.validateMobile(smsMessage.getMobile(), Constant.REGEX_PHONE))
            throw new BusinessException("手机号格式不正确，请重新输入");
        CloudAccount account = new CloudAccount(accessId, accessSecret, endpoint);
        MNSClient client = account.getMNSClient();
        CloudTopic topic = client.getTopicRef(smsTopic);
        RawTopicMessage msg = new RawTopicMessage();
        msg.setMessageBody("sms-message");
        MessageAttributes messageAttributes = new MessageAttributes();
        BatchSmsAttributes batchSmsAttributes = new BatchSmsAttributes();
        batchSmsAttributes.setFreeSignName(StringUtils.convertISOToUTF(signName));
        batchSmsAttributes.setTemplateCode(smsMessage.getTemplateId());
        BatchSmsAttributes.SmsReceiverParams smsReceiverParams = new BatchSmsAttributes.SmsReceiverParams();
        smsMessage.getTemplateParams().forEach((key,value)-> smsReceiverParams.setParam(key, value.toString()));
        // 设置接收短信的号码
        batchSmsAttributes.addSmsReceiver(smsMessage.getMobile(), smsReceiverParams);
        messageAttributes.setBatchSmsAttributes(batchSmsAttributes);
        try {
            TopicMessage responseResult = topic.publishMessage(msg, messageAttributes);
            System.out.println("MessageId: " + responseResult.getMessageId());
            System.out.println("MessageMD5: " + responseResult.getMessageBodyMD5());
        } catch (ServiceException se) {
            System.out.println(se.getErrorCode() + se.getRequestId());
            System.out.println(se.getMessage());
            logger.error("发送短信失败",se);
            throw new BusinessException("发送短信失败");
        } catch (Exception e) {
            logger.error("发送短信失败",e);
            throw new BusinessException("发送短信失败");
        }
        client.close();
    }
}
