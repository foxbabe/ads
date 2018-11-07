package com.sztouyun.advertisingsystem.common.annotation.validation;

import com.sztouyun.advertisingsystem.common.EnumMessage;
import com.sztouyun.advertisingsystem.utils.EnumUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EnumValueValidator implements ConstraintValidator<EnumValue, Integer> {
    private Class<? extends EnumMessage<Integer>> enumClass;
    private boolean nullable=false;

    @Override
    public void initialize(EnumValue enumValue) {
        enumClass = enumValue.enumClass();
        nullable=enumValue.nullable();
    }

    @Override
    public boolean isValid(Integer integer, ConstraintValidatorContext constraintValidatorContext) {
        if(nullable && null==integer)
            return true;
        return EnumUtils.isValidValue(integer,enumClass);
    }
}
