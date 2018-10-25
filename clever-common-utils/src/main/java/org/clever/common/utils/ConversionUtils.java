package org.clever.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Conversion;

import java.math.BigDecimal;

/**
 * 对象类型转换工具类，使用类型强制转换实现<br/>
 * 1.把一种类型转换成另一种类型<br/>
 *
 * @author LiZhiWei
 * @version 2015年6月21日 上午11:32:12
 */
@Slf4j
public class ConversionUtils extends Conversion {
    /**
     * 把一种类型，通过类型强制转换成另一种类型，转换失败抛出异常<br/>
     *
     * @param object 待转换的数据
     */
    @SuppressWarnings("unchecked")
    public static <E> E converter(Object object) {
        return (E) object;
    }

    /**
     * 把一种类型，通过类型强制转换成另一种类型，转换失败不会抛出异常<br/>
     * 1.注意：defaultValue与返回值类型一定要一致<br/>
     *
     * @param object       待转换的数据
     * @param defaultValue 转换失败返回的默认值
     */
    @SuppressWarnings("unchecked")
    public static <E> E converter(Object object, E defaultValue) {
        if (object == null) {
            return defaultValue;
        }
        try {
            return (E) object;
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            return defaultValue;
        }
    }

    /**
     * 对象toString操作
     */
    public static String toString(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Integer
                || obj instanceof Long
                || obj instanceof Float
                || obj instanceof Double) {
            return new BigDecimal(obj.toString()).toString();
        }
        return obj.toString();
    }
}