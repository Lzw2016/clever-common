package org.clever.common.utils.mapper;

import org.clever.common.utils.exception.ExceptionUtils;
import org.clever.common.utils.mapper.cglib.BeanConverter;
import org.clever.common.utils.mapper.cglib.TypeConverter;
import org.clever.common.utils.mapper.cglib.converters.StringToDateConverter;
import org.clever.common.utils.mapper.cglib.converters.StringToNumberConverter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * JavaBean与JavaBean之间的转换，使用cglib的BeanCopier实现(性能极好)<br/>
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2019/12/19 14:30 <br/>
 */
public class CgLibBeanMapper {

    private static final BeanConverter BEAN_CONVERTER;

    static {
        BEAN_CONVERTER = new BeanConverter();
        BEAN_CONVERTER.registerConverter(new StringToDateConverter())
                .registerConverter(new StringToNumberConverter());
    }

    public static void registerConverter(TypeConverter converter) {
        BEAN_CONVERTER.registerConverter(converter);
    }

    /**
     * 将对象source的值拷贝到对象destinationObject中<br/>
     * <b>注意：此操作source中的属性会覆盖destinationObject中的属性，无论source中的属性是不是空的</b><br/>
     *
     * @param source            数据源对象
     * @param destinationObject 目标对象
     */
    public static void copyTo(Object source, Object destinationObject) {
        BEAN_CONVERTER.copyTo(source, destinationObject);
    }

    /**
     * 不同类型的JavaBean对象转换<br/>
     *
     * @param source           数据源JavaBean
     * @param destinationClass 需要转换之后的JavaBean类型
     * @param <T>              需要转换之后的JavaBean类型
     * @return 转换之后的对象
     */
    public static <T> T mapper(Object source, Class<T> destinationClass) {
        return BEAN_CONVERTER.mapper(source, destinationClass);
    }

    /**
     * 将对象source的值拷贝到对象destinationObject中<br/>
     * <b>注意：此操作source中的属性会覆盖destinationObject中的属性，无论source中的属性是不是空的</b><br/>
     *
     * @param source            数据源对象
     * @param destinationObject 目标对象
     * @param typeConverters    自定义转换函数
     */
    public static void copyTo(Object source, Object destinationObject, TypeConverter... typeConverters) {
        BeanConverter tmp = BEAN_CONVERTER.newExtBeanConverter(typeConverters);
        tmp.copyTo(source, destinationObject);
    }

    /**
     * 不同类型的JavaBean对象转换<br/>
     *
     * @param source           数据源JavaBean
     * @param destinationClass 需要转换之后的JavaBean类型
     * @param typeConverters   自定义转换函数
     * @param <T>              需要转换之后的JavaBean类型
     * @return 转换之后的对象
     */
    public static <T> T mapper(Object source, Class<T> destinationClass, TypeConverter... typeConverters) {
        BeanConverter tmp = BEAN_CONVERTER.newExtBeanConverter(typeConverters);
        return tmp.mapper(source, destinationClass);
    }

    /**
     * 不同类型的JavaBean对象转换，基于Collection(集合)的批量转换<br/>
     * 如：List&lt;ThisBean&gt; <---->  List&lt;OtherBean&gt;<br/>
     *
     * @param sourceList       数据源JavaBean集合
     * @param destinationClass 需要转换之后的JavaBean类型
     * @param <T>              需要转换之后的JavaBean类型
     * @return 转换之后的对象集合
     */
    public static <T> List<T> mapperCollection(Collection<?> sourceList, Class<T> destinationClass) {
        if (sourceList == null) {
            return null;
        }
        try {
            List<T> destinationList = new ArrayList<>();
            for (Object sourceObject : sourceList) {
                T destinationObject = mapper(sourceObject, destinationClass);
                destinationList.add(destinationObject);
            }
            return destinationList;
        } catch (Throwable e) {
            throw ExceptionUtils.unchecked(e);
        }
    }
}
