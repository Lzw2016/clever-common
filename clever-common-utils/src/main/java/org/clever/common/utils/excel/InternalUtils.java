package org.clever.common.utils.excel;

import com.alibaba.excel.annotation.ExcelColumnNum;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.ExcelColumnProperty;
import com.alibaba.excel.util.TypeUtil;
import net.sf.cglib.beans.BeanMap;
import org.clever.common.utils.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 内部工具
 * 作者： lzw<br/>
 * 创建时间：2019-05-14 17:58 <br/>
 */
class InternalUtils {

    /**
     * 获取Excel头信息
     *
     * @param field 类型字段
     */
    public static ExcelColumnProperty getExcelColumnProperty(Field field) {
        ExcelColumnProperty excelHeadProperty = null;
        ExcelProperty excelProperty = field.getAnnotation(ExcelProperty.class);
        if (excelProperty != null) {
            excelHeadProperty = new ExcelColumnProperty();
            excelHeadProperty.setField(field);
            excelHeadProperty.setHead(Arrays.stream(excelProperty.value()).map(StringUtils::trim).collect(Collectors.toList()));
            excelHeadProperty.setIndex(excelProperty.index());
            excelHeadProperty.setFormat(excelProperty.format());
        }
        if (excelHeadProperty == null) {
            ExcelColumnNum columnNum = field.getAnnotation(ExcelColumnNum.class);
            if (columnNum != null) {
                excelHeadProperty = new ExcelColumnProperty();
                excelHeadProperty.setField(field);
                excelHeadProperty.setIndex(columnNum.value());
                excelHeadProperty.setFormat(columnNum.format());
            }
        }
        return excelHeadProperty;
    }

    public static <T> List<String> getRowList(T rowData, List<ExcelColumnProperty> columnPropertyList) {
        BeanMap beanMap = BeanMap.create(rowData);
        List<String> rowList = new ArrayList<>();
        for (ExcelColumnProperty excelColumnProperty : columnPropertyList) {
            String cellValue = TypeUtil.getFieldStringValue(beanMap, excelColumnProperty.getField().getName(), excelColumnProperty.getFormat());
            rowList.add(cellValue);
        }
        return rowList;
    }
}
