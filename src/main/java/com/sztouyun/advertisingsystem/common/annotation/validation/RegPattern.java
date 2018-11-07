package com.sztouyun.advertisingsystem.common.annotation.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Created by szty on 2018/6/25.
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {RegValidator.class})
public @interface RegPattern {
    String regexp();
    String message() default "格式不正确";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default {};

    boolean nullable() default false;
}
