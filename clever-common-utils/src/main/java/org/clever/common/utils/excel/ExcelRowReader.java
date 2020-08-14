package org.clever.common.utils.excel;

import org.clever.common.utils.excel.dto.ExcelRow;

/**
 * 作者： lzw<br/>
 * 创建时间：2019-05-13 17:44 <br/>
 */
public interface ExcelRowReader<T> {
    /**
     * 处理Excel数据行
     *
     * @param data     校验通过的数据
     * @param excelRow 数据行对象
     */
    void readerRow(T data, ExcelRow<T> excelRow);
}
