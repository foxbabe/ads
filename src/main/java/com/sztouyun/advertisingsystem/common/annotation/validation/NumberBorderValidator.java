package com.sztouyun.advertisingsystem.common.annotation.validation;

import com.sztouyun.advertisingsystem.common.Constant;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NumberBorderValidator implements ConstraintValidator<NumberBorder, Number> {
    private double border ;
    private boolean nullable;
    private BorderType type;

    @Override
    public void initialize(NumberBorder numberBorder) {
        nullable=numberBorder.nullable();
        type=numberBorder.type();
        border=numberBorder.border();

    }

    @Override
    public boolean isValid(Number value, ConstraintValidatorContext constraintValidatorContext) {
        if(nullable && null==value)
            return true;
        double param=value.doubleValue();
        if(type.equals(BorderType.MIN) && param <border)
            return false;
        if(type.equals(BorderType.MAX) && ( param >border || param<0))
            return false;
        return true;
    }
}
