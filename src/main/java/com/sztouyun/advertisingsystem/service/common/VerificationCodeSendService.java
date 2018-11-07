package com.sztouyun.advertisingsystem.service.common;

import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.sms.ISmsService;
import com.sztouyun.advertisingsystem.common.sms.SmsMessage;
import com.sztouyun.advertisingsystem.common.verificationCode.IVerificationCodeGenerator;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.account.User;
import com.sztouyun.advertisingsystem.model.common.QVerificationCodeSendLog;
import com.sztouyun.advertisingsystem.model.common.VerificationCodeSendLog;
import com.sztouyun.advertisingsystem.repository.account.UserRepository;
import com.sztouyun.advertisingsystem.repository.common.VerificationCodeSendLogRepository;
import com.sztouyun.advertisingsystem.service.BaseService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class VerificationCodeSendService extends BaseService {
    @Autowired
    private IVerificationCodeGenerator codeGenerator;
    @Autowired
    private ISmsService smsService;
    @Autowired
    private VerificationCodeSendLogRepository verificationCodeSendLogRepository;


    private QVerificationCodeSendLog qVerificationCodeSendLog = QVerificationCodeSendLog.verificationCodeSendLog;
    @Value("${verification.code.template.id}")
    private String templateId;

    public void sendVerificationCode(String mobile,VerificationCodeTypeEnum type,int codeLength,int intervalSecond){

        Date lastSendTime = verificationCodeSendLogRepository.findOne(f->f.select(qVerificationCodeSendLog.createdTime).from(qVerificationCodeSendLog)
                .where(qVerificationCodeSendLog.mobile.eq(mobile).and(qVerificationCodeSendLog.type.eq(type.getValue()))).orderBy(qVerificationCodeSendLog.createdTime.desc()));
        if(lastSendTime !=null && (new Date().getTime() - lastSendTime.getTime() < intervalSecond * 1000))
            throw new BusinessException("短信验证码已发送,请60秒后重试");

        Date today = DateTime.now().toLocalDate().toDate();
        Date tomorrow = DateTime.now().toLocalDate().plusDays(1).toDate();
        if(verificationCodeSendLogRepository.findOne(f->f.select(qVerificationCodeSendLog.id.count()).from(qVerificationCodeSendLog).where(qVerificationCodeSendLog.mobile.eq(mobile)
                .and(qVerificationCodeSendLog.createdTime.goe(today))
                .and(qVerificationCodeSendLog.createdTime.before(tomorrow)))) >= Constant.SMSMAXTIMESPERDAY)
            throw new BusinessException("短信验证码次数请求超限,请24小时后重试");

        String code = codeGenerator.generate(codeLength);
        Map<String, Object> templateParams =new HashMap<>();
        templateParams.put("code",code);
        smsService.sendMessage(new SmsMessage(templateId,templateParams,mobile));
        verificationCodeSendLogRepository.save(new VerificationCodeSendLog(mobile,code,type.getValue()));
    }
}
