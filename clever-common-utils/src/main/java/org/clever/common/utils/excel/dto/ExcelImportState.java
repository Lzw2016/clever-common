package org.clever.common.utils.excel.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 作者： lzw<br/>
 * 创建时间：2019-05-13 11:50 <br/>
 */
@Data
public class ExcelImportState implements Serializable {
    /**
     * 导入是否成功
     */
    private boolean success;

    /**
     * 总数据量
     */
    private int totalRows;

    /**
     * 成功数据量
     */
    private int successRows;

    /**
     * 失败数据量
     */
    private int failRows;

    /**
     * 错误数量
     */
    private int errorCount;

    /**
     * 重复数据量
     */
    private int repeat;
}
