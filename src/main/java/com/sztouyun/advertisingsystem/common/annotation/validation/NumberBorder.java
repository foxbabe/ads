package com.sztouyun.advertisingsystem.common.annotation.validation;


import com.sztouyun.advertisingsystem.common.Constant;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {NumberBorderValidator.class})
public @interface NumberBorder {
    String message() default "请输入有效的数值";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default {};

    boolean nullable() default false;

    BorderType type() default BorderType.MIN;

    double border() default 0;

}
