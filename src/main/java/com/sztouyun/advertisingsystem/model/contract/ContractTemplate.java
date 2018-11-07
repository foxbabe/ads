package com.sztouyun.advertisingsystem.model.contract;

import com.sztouyun.advertisingsystem.model.BaseModel;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class ContractTemplate extends BaseModel{

    /**
     * 合同模板url
     */
    @Column(length = 128)
    private String url;

    /**
     * 合同模板类型
     */
    private Integer templateType;

    /**
     * 合同模板名称
     */
    @Column(length = 50)
    private String templateName;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getTemplateType() {
        return templateType;
    }

    public void setTemplateType(Integer templateType) {
        this.templateType = templateType;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }
}
