package com.sztouyun.advertisingsystem.service.common;


import com.sztouyun.advertisingsystem.model.common.CodeTypeEnum;

public interface CodeGenerationService {
    String generateCode(CodeTypeEnum codeTypeEnum);

    String generateCode(String codeType,int length);
}
