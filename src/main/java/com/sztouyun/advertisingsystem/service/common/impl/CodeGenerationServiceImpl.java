package com.sztouyun.advertisingsystem.service.common.impl;


import com.sztouyun.advertisingsystem.mapper.CodeGenerationMapper;
import com.sztouyun.advertisingsystem.model.common.CodeRule;
import com.sztouyun.advertisingsystem.model.common.CodeTypeEnum;
import com.sztouyun.advertisingsystem.repository.common.CodeGenerationRepository;
import com.sztouyun.advertisingsystem.service.BaseService;
import com.sztouyun.advertisingsystem.service.common.CodeGenerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CodeGenerationServiceImpl extends BaseService implements CodeGenerationService {
    private static final int INIT_CODE_NUMBER = 1;//初始化编码
    private static final int DEFAULT_CODE_LENGTH = 6;//默认编码长度
    @Autowired
    private CodeGenerationMapper codeGenerationMapper;

    @Autowired
    private CodeGenerationRepository codeGenerationRepository;

    @Override
    public String generateCode(CodeTypeEnum codeTypeEnum) {
        return generateCode(codeTypeEnum.toString(),DEFAULT_CODE_LENGTH);
    }

    @Override
    @Transactional
    public String generateCode(String codeType,int length) {
        CodeRule codeRule = codeGenerationMapper.findByCodeType(codeType);

        if (codeRule == null) {
            codeRule = new CodeRule();
            codeRule.setCodeNumber(INIT_CODE_NUMBER);
            codeRule.setCodeType(codeType);
        } else {
            Integer codeNumber = codeRule.getCodeNumber();
            codeRule.setCodeNumber(++codeNumber);
            //如果编码长度超过指定长度，则取实际长度
            Integer codeLength = codeNumber.toString().length();
            if(codeLength > length){
                length = codeLength;
            }
        }
        codeGenerationRepository.save(codeRule);
        return codeType + String.format("%0"+length+"d", codeRule.getCodeNumber());
    }
}
