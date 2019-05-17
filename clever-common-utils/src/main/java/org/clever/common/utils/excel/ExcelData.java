package org.clever.common.utils.excel;

import com.alibaba.excel.metadata.ExcelColumnProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 以Json格式返回给前端显示，数据结构如下：
 * <pre>{@code
 *  {
 *      clazz: "...",
 *      headRowNum: 1,
 *      heads: [],
 *      excelImportState: {},
 *      importData: [],
 *      failRows: [],
 *  }
 * }</pre>
 * <p>
 * 作者： lzw<br/>
 * 创建时间：2019-05-13 11:41 <br/>
 */
public class ExcelData<T> implements Serializable {

    /**
     * 数据类型
     */
    @Getter
    private Class<T> clazz;

    /**
     * 表头信息
     */
    @Getter
    private List<ExcelHead> heads = new ArrayList<>();

    /**
     * Excel行数据
     */
    @JsonIgnore
    @Getter
    private List<ExcelRow<T>> rows = new ArrayList<>();

    /**
     * 导入的数据前端使用
     */
    private List<?> importData;

    /**
     * Excel导出统计
     */
    private ExcelImportState excelImportState = new ExcelImportState();

    /**
     * @param clazz Excel解析对应的数据类型
     */
    public ExcelData(Class<T> clazz, List<ExcelColumnProperty> columnProperties) {
        this.clazz = clazz;
        for (ExcelColumnProperty columnProperty : columnProperties) {
            heads.add(new ExcelHead(columnProperty));
        }
    }

    /**
     * 清除导入数据
     */
    public synchronized void clearData() {
        if (rows == null) {
            rows = new ArrayList<>();
        }
        rows.clear();
        importData = null;
        excelImportState = new ExcelImportState();
    }

    /**
     * Excel表格头所占行数(复杂嵌套表格头行数大于1)
     */
    public int getHeadRowNum() {
        int headRowNum = 0;
        for (ExcelHead excelHeads : heads) {
            if (excelHeads != null && excelHeads.getHead() != null && excelHeads.getHead().size() > 0) {
                if (excelHeads.getHead().size() > headRowNum) {
                    headRowNum = excelHeads.getHead().size();
                }
            }
        }
        return headRowNum;
    }

    /**
     * 当前解析的数据是否有失败的
     */
    public boolean hasError() {
        if (rows == null || rows.size() <= 0) {
            return false;
        }
        for (ExcelRow<T> excelRow : rows) {
            if (excelRow.hasError()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 增加成功数据
     *
     * @return 增加成功返回true，数据重复返回false
     */
    public boolean addRow(ExcelRow<T> excelRow) {
        if (excelRow == null) {
            return false;
        }
        boolean repeat = rows.stream().filter(tmp -> Objects.equals(tmp.getDataSignature(), excelRow.getDataSignature())).findFirst().orElse(null) != null;
        if (repeat) {
            addRepeat(1);
            return false;
        }
        return rows.add(excelRow);
    }

    @JsonIgnore
    public List<ExcelRow<T>> getSuccessRows() {
        if (rows == null || rows.size() <= 0) {
            return new ArrayList<>();
        }
        return rows.stream().filter(excelRow -> !excelRow.hasError()).collect(Collectors.toList());
    }

    public List<ExcelRow<T>> getFailRows() {
        if (rows == null || rows.size() <= 0) {
            return new ArrayList<>();
        }
        return rows.stream().filter(ExcelRow::hasError).collect(Collectors.toList());
    }

    @JsonIgnore
    public List<T> getSuccessData() {
        if (rows == null || rows.size() <= 0) {
            return new ArrayList<>();
        }
        return rows.stream().filter(excelRow -> !excelRow.hasError()).map(ExcelRow::getData).collect(Collectors.toList());
    }

    @JsonIgnore
    public List<T> getFailData() {
        if (rows == null || rows.size() <= 0) {
            return new ArrayList<>();
        }
        return rows.stream().filter(ExcelRow::hasError).map(ExcelRow::getData).collect(Collectors.toList());
    }

    /**
     * 返回导入成功的数据
     */
    public List<?> getImportData() {
        if (importData == null) {
            importData = getSuccessData();
        }
        return importData;
    }

    /**
     * 设置导入成功的数据
     */
    public void setImportData(List<?> importData) {
        this.importData = importData;
    }

//    /**
//     * 设置重复的数据量
//     *
//     * @param repeat 重复数据量
//     */
//    public synchronized void setRepeat(int repeat) {
//        if (excelImportState == null) {
//            excelImportState = new ExcelImportState();
//        }
//        excelImportState.setRepeat(repeat);
//    }

    /**
     * 增加重复数据数量
     *
     * @param repeat 增加的数量
     */
    @SuppressWarnings("SameParameterValue")
    private synchronized void addRepeat(int repeat) {
        if (excelImportState == null) {
            excelImportState = new ExcelImportState();
        }
        excelImportState.setRepeat(excelImportState.getRepeat() + repeat);
    }

    public ExcelImportState getExcelImportState() {
        if (excelImportState == null) {
            excelImportState = new ExcelImportState();
        }
        List<ExcelRow<T>> failRows = rows.stream().filter(ExcelRow::hasError).collect(Collectors.toList());
        excelImportState.setSuccess(!hasError());
        excelImportState.setTotalRows(rows.size() + excelImportState.getRepeat());
        excelImportState.setSuccessRows(rows.size() - failRows.size());
        excelImportState.setFailRows(failRows.size());
        int errorCount = 0;
        for (ExcelRow<T> excelRow : failRows) {
            errorCount += excelRow.getRowError().size();
            for (List<String> errors : excelRow.getColumnError().values()) {
                errorCount += errors.size();
            }
        }
        excelImportState.setErrorCount(errorCount);
        return excelImportState;
    }
}
