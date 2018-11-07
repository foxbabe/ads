package com.sztouyun.advertisingsystem.common.annotation.validation;


import com.sztouyun.advertisingsystem.common.EnumMessage;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {EnumValueValidator.class})
public @interface EnumValue {
    String message() default "不支持本类型";

    boolean nullable() default false;

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default {};

    Class<? extends EnumMessage<Integer>> enumClass();
}
