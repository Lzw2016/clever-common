package org.clever.common.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-10-22 17:07 <br/>
 */
@Target(METHOD)
@Retention(RUNTIME)
public @interface NotDecodeSlash {
}