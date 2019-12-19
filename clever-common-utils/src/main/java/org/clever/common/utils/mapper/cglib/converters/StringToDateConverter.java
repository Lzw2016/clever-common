package org.clever.common.utils.mapper.cglib.converters;

import org.apache.commons.lang3.ClassUtils;
import org.clever.common.utils.DateTimeUtils;
import org.clever.common.utils.mapper.cglib.TypeConverter;

import java.util.Date;

/**
 * 作者：lizw <br/>
 * 创建时间：2019/12/19 14:31 <br/>
 */
public class StringToDateConverter implements TypeConverter {
    @Override
    public boolean support(Object source, Class<?> targetType) {
        if (source == null) {
            return false;
        }
        return ClassUtils.isAssignable(source.getClass(), String.class, true)
                && ClassUtils.isAssignable(targetType, Date.class, true);
    }

    @Override
    public Object convert(Object source, Class<?> targetType, Object context) {
        return DateTimeUtils.parseDate(source);
    }
}
