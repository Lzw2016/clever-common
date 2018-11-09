package org.clever.common.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 状态校验 Integer 类型
 * <p>
 * 作者： lzw<br/>
 * 创建时间：2018-11-09 21:27 <br/>
 */
@Documented
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Constraint(validatedBy = StatusIntegerValidator.class)
public @interface ValidIntegerStatus {

    String message() default "状态校验失败";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * 有效的状态值集合
     */
    int[] value() default {};
}
