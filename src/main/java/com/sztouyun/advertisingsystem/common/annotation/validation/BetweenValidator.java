package com.sztouyun.advertisingsystem.common.annotation.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class BetweenValidator implements ConstraintValidator<Between, Double> {

    private double min ;
    private double max ;
    private boolean includeMin;
    private boolean includeMax;

    @Override
    public void initialize(Between between) {
        min = between.min();
        max = between.max();
        includeMin = between.includeMin();
        includeMax = between.includeMax();

    }

    @Override
    public boolean isValid(Double value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null)
            return false;

        if(!includeMin && value.equals(min))
            return false;

        if(!includeMax && value.equals(max))
            return false;

        if (value >= min && value <= max )
            return true;

        return false;
    }
}
