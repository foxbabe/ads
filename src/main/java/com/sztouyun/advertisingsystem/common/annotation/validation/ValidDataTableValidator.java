package com.sztouyun.advertisingsystem.common.annotation.validation;

import com.sztouyun.advertisingsystem.common.datatable.DataTable;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidDataTableValidator implements ConstraintValidator<ValidDataTable, DataTable> {
    @Override
    public void initialize(ValidDataTable dataTable) {
    }

    @Override
    public boolean isValid(DataTable value, ConstraintValidatorContext constraintValidatorContext) {
        if(value == null)
            return true;
        value.validate();
        return true;
    }
}
