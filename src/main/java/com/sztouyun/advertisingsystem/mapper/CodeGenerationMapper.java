package com.sztouyun.advertisingsystem.mapper;

import com.sztouyun.advertisingsystem.model.common.CodeRule;
import org.apache.ibatis.annotations.Select;
public interface CodeGenerationMapper {
    @Select("select code_type as codeType, code_number as codeNumber from code_rule where code_type=#{codeType} FOR UPDATE")
    CodeRule findByCodeType(String codeType);
}
