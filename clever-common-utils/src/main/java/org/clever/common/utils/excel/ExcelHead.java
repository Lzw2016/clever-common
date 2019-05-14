package org.clever.common.utils.excel;

import com.alibaba.excel.metadata.ExcelColumnProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者： lzw<br/>
 * 创建时间：2019-05-13 11:44 <br/>
 */
@Data
public class ExcelHead implements Serializable {

    /**
     * 自动格式化配置
     */
    private String format;

    /**
     * 字段位置
     */
    private Integer index;

    /**
     * Excel表头名称(允许多级表头)
     */
    private List<String> head = new ArrayList<>();

    /**
     * 第一级表头
     */
    private String firstHead;

    /**
     * 最后一级表头(建议前端使用此值)
     */
    private String lastHead;

    /**
     * 导入实体类字段名
     */
    private String columnName;

    /**
     * 导入实体类字段类型
     */
    private Class<?> columnType;

    public ExcelHead(ExcelColumnProperty excelColumnProperty) {
        format = excelColumnProperty.getFormat();
        index = excelColumnProperty.getIndex();
        head.addAll(excelColumnProperty.getHead());
        if (head.size() > 0) {
            firstHead = head.get(0);
            lastHead = head.get(head.size() - 1);
        }
        columnName = excelColumnProperty.getField().getName();
        columnType = excelColumnProperty.getField().getType();
    }
}
