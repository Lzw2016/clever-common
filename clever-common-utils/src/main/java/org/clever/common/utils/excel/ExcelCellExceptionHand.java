package org.clever.common.utils.excel;

import com.alibaba.excel.metadata.ExcelColumnProperty;

import java.util.List;

/**
 * 处理单元格异常
 *
 * 作者： lzw<br/>
 * 创建时间：2019-05-24 15:31 <br/>
 */
public interface ExcelCellExceptionHand<T> {

    /**
     * 处理单元格异常
     * <pre>
     * {@code
     *  columnProperty.getField().getName()     获取当前列对应实体属性字段名称
     *  columnProperty.getHead()                获取当前列头名称(可能有多个)
     *  columnProperty.getFormat()              获取当前列头format配置
     * }
     * </pre>
     *
     * @param throwable           当前行异常
     * @param cellStr             当前处理异常的单元格
     * @param rowStrList          行数据集合
     * @param excelRow            当前行对象
     * @param excelColumnProperty 当前列元数据
     */
    void exceptionHand(Throwable throwable, String cellStr, List<String> rowStrList, ExcelRow<T> excelRow, ExcelColumnProperty excelColumnProperty);
}
