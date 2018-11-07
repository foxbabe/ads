package com.sztouyun.advertisingsystem.common.annotation.validation;


import com.sztouyun.advertisingsystem.common.Constant;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * Created by szty on 2018/6/25.
 */
public class RegValidator implements ConstraintValidator<RegPattern, String> {
    private String regexp ;
    private boolean nullable;
    @Override
    public void initialize(RegPattern regPattern) {
        regexp=regPattern.regexp();
        nullable=regPattern.nullable();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return nullable && StringUtils.isEmpty(value)?true:Pattern.compile(regexp).matcher(value).matches();
    }
}
