package com.sztouyun.advertisingsystem.model.contract;

import com.sztouyun.advertisingsystem.common.EnumMessage;

public enum ContractTemplateTypeEnum  implements EnumMessage<Integer> {

    REQUIRE_FEES(1, "收费合同模板","pdf"),
    FREE(2, "免费合同模板","pdf"),
    COMMON(3, "通用模板","pdf"),
    FREEMARKER(4, "新版收费模板","ftl");


    private Integer value;
    private String displayName;
    private String ext;

    ContractTemplateTypeEnum(Integer value,String displayName,String ext) {
        this.value = value;
        this.displayName = displayName;
        this.ext=ext;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    public String getExt() {
        return ext;
    }
}
