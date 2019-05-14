package org.clever.common.utils.excel;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.annotation.ExcelColumnNum;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.exception.ExcelGenerateException;
import com.alibaba.excel.metadata.ExcelColumnProperty;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.metadata.Table;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.util.TypeUtil;
import com.google.common.collect.Lists;
import net.sf.cglib.beans.BeanMap;
import org.apache.commons.lang3.StringUtils;
import org.clever.common.utils.codec.EncodeDecodeUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 作者： lzw<br/>
 * 创建时间：2019-05-14 17:58 <br/>
 */
public class ExcelDataWriter {

    /**
     * 导出Excel给浏览器下载
     *
     * @param request   请求
     * @param response  响应
     * @param fileName  文件名称
     * @param clazz     Excel页签对应的数据类型
     * @param data      Excel页签对应的数据集合
     * @param sheetName Excel页签名
     */
    public static <T> void exportExcel(HttpServletRequest request, HttpServletResponse response, String fileName, Class<T> clazz, List<T> data, String sheetName) {
        ExcelDataWriter excelDataWriter = new ExcelDataWriter();
        excelDataWriter.addSheet(clazz, data, sheetName);
        excelDataWriter.exportExcel(request, response, fileName);
    }

    /**
     * 导出Excel给浏览器下载
     *
     * @param request  请求
     * @param response 响应
     * @param fileName 文件名称
     * @param clazz    Excel页签对应的数据类型
     * @param data     Excel页签对应的数据集合
     */
    public static <T> void exportExcel(HttpServletRequest request, HttpServletResponse response, String fileName, Class<T> clazz, List<T> data) {
        ExcelDataWriter excelDataWriter = new ExcelDataWriter();
        excelDataWriter.addSheet(clazz, data);
        excelDataWriter.exportExcel(request, response, fileName);
    }

    /**
     * 导出Excel给浏览器下载
     *
     * @param request  请求
     * @param response 响应
     * @param clazz    Excel页签对应的数据类型
     * @param data     Excel页签对应的数据集合
     */
    public static <T> void exportExcel(HttpServletRequest request, HttpServletResponse response, Class<T> clazz, List<T> data) {
        ExcelDataWriter excelDataWriter = new ExcelDataWriter();
        excelDataWriter.addSheet(clazz, data);
        excelDataWriter.exportExcel(request, response, null);
    }

    /**
     * Excel页签名
     */
    private List<String> sheetNameList = Lists.newArrayList();

    /**
     * Excel页签对应的数据类型
     */
    private List<Class> sheetClassList = Lists.newArrayList();

    /**
     * Excel页签对应的数据集合
     */
    private List<List<?>> sheetDataList = Lists.newArrayList();

    /**
     * 增加导出Excel数据页签
     *
     * @param clazz     Excel页签对应的数据类型
     * @param data      Excel页签对应的数据集合
     * @param sheetName Excel页签名
     */
    public synchronized <T> ExcelDataWriter addSheet(Class<T> clazz, List<T> data, String sheetName) {
        sheetDataList.add(data);
        sheetClassList.add(clazz);
        sheetNameList.add(sheetName);
        return this;
    }

    /**
     * 增加导出Excel数据页签
     *
     * @param clazz Excel页签对应的数据类型
     * @param data  Excel页签对应的数据集合
     */
    public synchronized <T> ExcelDataWriter addSheet(Class<T> clazz, List<T> data) {
        return addSheet(clazz, data, String.format("sheet%s", sheetNameList.size() + 1));
    }

    /**
     * 移除导出Excel数据叶签
     *
     * @param index Excel叶签位置(从0开始)
     */
    public synchronized boolean removeSheet(int index) {
        if (sheetNameList.size() <= index) {
            return false;
        }
        sheetNameList.remove(index);
        sheetClassList.remove(index);
        sheetDataList.remove(index);
        return true;
    }

    /**
     * 移除导出Excel数据叶签
     *
     * @param sheetName Excel页签名
     */
    public synchronized boolean removeSheet(String sheetName) {
        List<String> sheetNameListTmp = new ArrayList<>();
        List<Class> sheetClassListTmp = Lists.newArrayList();
        List<List<?>> sheetDataListTmp = Lists.newArrayList();
        for (int index = 0; index < sheetNameList.size(); index++) {
            String name = sheetNameList.get(index);
            Class clzz = sheetClassList.get(index);
            List<?> sheetData = sheetDataList.get(index);
            if (!Objects.equals(sheetName, name)) {
                sheetNameListTmp.add(name);
                sheetClassListTmp.add(clzz);
                sheetDataListTmp.add(sheetData);
            }
        }
        if (sheetNameListTmp.size() == sheetNameList.size()) {
            return false;
        }
        sheetNameList = sheetNameListTmp;
        sheetClassList = sheetClassListTmp;
        sheetDataList = sheetDataListTmp;
        return true;
    }

    /**
     * 清除所有Excel导出数据
     */
    public synchronized void clearData() {
        sheetNameList.clear();
        sheetClassList.clear();
        sheetDataList.clear();
    }

    /**
     * 导出Excel给浏览器下载
     *
     * @param request  请求
     * @param response 响应
     * @param fileName 文件名称
     */
    public void exportExcel(HttpServletRequest request, HttpServletResponse response, String fileName) {
        if (StringUtils.isBlank(fileName)) {
            fileName = "数据导出.xlsx";
        }
        fileName = StringUtils.trim(fileName);
        if (!fileName.toLowerCase().endsWith(".xlsx") && !fileName.toLowerCase().endsWith(".xls")) {
            fileName = fileName + ".xlsx";
        }
        fileName = EncodeDecodeUtils.browserDownloadFileName(request.getHeader("User-Agent"), fileName);
        response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        try (OutputStream outputStream = response.getOutputStream()) {
            writeExcel(outputStream);
        } catch (IOException e) {
            throw new ExcelGenerateException("生成Excel文件失败", e);
        }
    }

    /**
     * 生成Excel得到Excel文件二进制数据
     */
    public byte[] writeExcel() {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            writeExcel(outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new ExcelGenerateException("生成Excel文件失败", e);
        }
    }

    /**
     * 生成Excel写入数据流
     *
     * @param outputStream 目标数据流
     */
    public void writeExcel(OutputStream outputStream) {
        ExcelWriter writer = new ExcelWriter(outputStream, ExcelTypeEnum.XLSX);
        int index = 0;
        for (List<?> sheetData : sheetDataList) {
            String sheetName = sheetNameList.get(index);
            Class<?> clazz = sheetClassList.get(index);
            index++;
            Sheet sheet = new Sheet(index);
            sheet.setSheetName(sheetName);
            List<ExcelColumnProperty> columnPropertyList = initColumnProperty(clazz);
            if (columnPropertyList.size() <= 0) {
                throw new ExcelGenerateException(String.format("Excel解析对象: %s，字段未使用@ExcelProperty或者@ExcelColumnNum修饰声明与Excel的映射关系", clazz));
            }
            //获取数据
            List<List<String>> sheetRows = new ArrayList<>();
            for (Object object : sheetData) {
                BeanMap beanMap = BeanMap.create(object);
                List<String> row = new ArrayList<>();
                for (ExcelColumnProperty excelColumnProperty : columnPropertyList) {
                    String cellValue = TypeUtil.getFieldStringValue(beanMap, excelColumnProperty.getField().getName(), excelColumnProperty.getFormat());
                    row.add(cellValue);
                }
                sheetRows.add(row);
            }
            //构造表头
            List<List<String>> headsByHorizontal = new ArrayList<>();
            int headRowNum = getHeadRowNum(columnPropertyList);
            int columnSize = 0;
            for (int rowNum = 0; rowNum < headRowNum; rowNum++) {
                List<String> headByRowNum = getHeadByRowNum(rowNum, columnPropertyList);
                headsByHorizontal.add(headByRowNum);
                if (columnSize < headByRowNum.size()) {
                    columnSize = headByRowNum.size();
                }
            }
            List<List<String>> headsByVertical = new ArrayList<>();
            for (int columnNum = 0; columnNum < columnSize; columnNum++) {
                headsByVertical.add(new ArrayList<>(headRowNum));
            }
            // 水平表头转垂直表头
            for (int columnNum = 0; columnNum < columnSize; columnNum++) {
                for (int rowNum = 0; rowNum < headRowNum; rowNum++) {
                    String horizontal = headsByHorizontal.get(rowNum).get(columnNum);
                    headsByVertical.get(columnNum).add(horizontal);
                }
            }
            Table table = new Table(index - 1);
            table.setHead(headsByVertical);
            //以数组方式生成数据
            writer.write0(sheetRows, sheet, table);
        }
        writer.finish();
    }

    /**
     * 读取表头数据
     *
     * @param rowNum             表头行数
     * @param columnPropertyList Excel表头信息集合
     */
    private List<String> getHeadByRowNum(int rowNum, List<ExcelColumnProperty> columnPropertyList) {
        List<String> headByRowNum = new ArrayList<>(columnPropertyList.size());
        for (ExcelColumnProperty columnProperty : columnPropertyList) {
            List<String> head = columnProperty.getHead();
            if (head.size() > rowNum) {
                headByRowNum.add(head.get(rowNum));
            } else {
                headByRowNum.add(head.get(head.size() - 1));
            }
        }
        return headByRowNum;
    }

    /**
     * 获取Excel表头的行数
     *
     * @param columnPropertyList Excel表头信息集合
     */
    private int getHeadRowNum(List<ExcelColumnProperty> columnPropertyList) {
        int headRowNum = 0;
        for (ExcelColumnProperty columnProperty : columnPropertyList) {
            if (columnProperty != null && columnProperty.getHead() != null && columnProperty.getHead().size() > 0) {
                if (columnProperty.getHead().size() > headRowNum) {
                    headRowNum = columnProperty.getHead().size();
                }
            }
        }
        return headRowNum;
    }

    /**
     * 根据Excel映射实体类型读取Excel表头
     *
     * @param clazz Excel映射实体类型
     */
    private List<ExcelColumnProperty> initColumnProperty(Class<?> clazz) {
        List<ExcelColumnProperty> columnPropertyList = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            ExcelProperty excelProperty = field.getAnnotation(ExcelProperty.class);
            ExcelColumnProperty excelHeadProperty = null;
            if (excelProperty != null) {
                excelHeadProperty = new ExcelColumnProperty();
                excelHeadProperty.setField(field);
                excelHeadProperty.setHead(Arrays.asList(excelProperty.value()));
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
            if (excelHeadProperty != null) {
                columnPropertyList.add(excelHeadProperty);
            }
        }
        return columnPropertyList;
    }
}
