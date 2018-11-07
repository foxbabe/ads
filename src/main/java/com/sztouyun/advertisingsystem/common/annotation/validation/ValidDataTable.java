package com.sztouyun.advertisingsystem.common.annotation.validation;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {ValidDataTableValidator.class})
public @interface ValidDataTable {
    String message() default "";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default {};
}
