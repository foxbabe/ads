package com.sztouyun.advertisingsystem.common.sms;

import java.util.HashMap;
import java.util.Map;

public class SmsMessage {
    private String templateId;
    private Map<String,Object> templateParams = new HashMap<>();
    private String mobile;

    public SmsMessage() {
    }

    public SmsMessage(String templateId, Map<String, Object> templateParams, String mobile) {
        this.templateId = templateId;
        this.templateParams = templateParams;
        this.mobile = mobile;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public Map<String, Object> getTemplateParams() {
        return templateParams;
    }

    public void setTemplateParams(Map<String, Object> templateParams) {
        this.templateParams = templateParams;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
