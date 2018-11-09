package org.clever.common.validation;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-11-09 21:49 <br/>
 */
public class StringNotBlankValidator implements ConstraintValidator<StringNotBlank, String> {

    @Override
    public boolean isValid(String str, ConstraintValidatorContext constraintValidatorContext) {
        if (str == null) {
            return true;
        }
        return StringUtils.isNotBlank(str);
    }
}
