package org.clever.common.utils.mapper;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;
import org.clever.common.utils.exception.ExceptionUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * JavaBean与Map<String,Object>互转工具类<br/>
 * 1.org.apache.commons.beanutils.BeanUtils实现<br/>
 * <p>
 * 作者：LiZW <br/>
 * 创建时间：2016-4-30 0:44 <br/>
 */
@Slf4j
public class BeanMapConverter {
    /**
     * 把Map转换成JavaBean对象<br/>
     *
     * @param bean       JavaBean对象
     * @param properties Map集合
     * @return 成功返回true
     */
    public static boolean toObject(Object bean, Map<String, Object> properties) {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                if (properties.containsKey(key)) {
                    Object value = properties.get(key);
                    // 得到property对应的setter方法
                    Method setter = property.getWriteMethod();
                    if (setter != null) {
                        setter.invoke(bean, value);
                    }
                }
            }
            // BeanUtils.populate(bean, properties);
        } catch (Throwable e) {
            log.error("把Map转换成JavaBean对象出错", e);
            return false;
        }
        return true;
    }

    /**
     * 把JavaBean对象转换成Map<String, Object>
     *
     * @param bean JavaBean对象
     * @return 转换后的Map对象，失败返回null
     */
    public static Map<String, Object> toMap(Object bean) {
        Map<String, Object> map = null;
        try {
            map = PropertyUtils.describe(bean);
        } catch (Throwable e) {
            throw ExceptionUtils.unchecked(e);
        }
        return map;
    }
}
