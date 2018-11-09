package org.clever.common.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 状态校验 String 类型
 * 作者： lzw<br/>
 * 创建时间：2018-11-09 21:40 <br/>
 */
@Documented
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Constraint(validatedBy = StatusStringValidator.class)
public @interface ValidStringStatus {

    String message() default "状态校验失败";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * 有效的状态值集合
     */
    String[] value() default {};
}
