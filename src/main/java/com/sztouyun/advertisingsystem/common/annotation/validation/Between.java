package com.sztouyun.advertisingsystem.common.annotation.validation;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {BetweenValidator.class})
public @interface Between {
    String message() default "请输入有效的数值";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default {};

    double min();

    double max();

    boolean includeMin() default true;

    boolean includeMax() default true;
}
