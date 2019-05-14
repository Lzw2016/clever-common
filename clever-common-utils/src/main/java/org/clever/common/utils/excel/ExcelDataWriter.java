package org.clever.common.utils.excel;//package com.jzt.b2b3.excel;
//
//import com.alibaba.excel.ExcelWriter;
//import com.alibaba.excel.annotation.ExcelColumnNum;
//import com.alibaba.excel.annotation.ExcelProperty;
//import com.alibaba.excel.metadata.ExcelColumnProperty;
//import com.alibaba.excel.metadata.Sheet;
//import com.alibaba.excel.metadata.Table;
//import com.alibaba.excel.support.ExcelTypeEnum;
//import com.alibaba.excel.util.TypeUtil;
//import com.google.common.base.Strings;
//import com.google.common.collect.Lists;
//import lombok.val;
//import lombok.var;
//import net.sf.cglib.beans.BeanMap;
//
//import javax.servlet.http.HttpServletResponse;
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.OutputStream;
//import java.lang.reflect.Field;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
///**
// * 作者： lzw<br/>
// * 创建时间：2019-05-14 17:58 <br/>
// */
//@SuppressWarnings("Duplicates")
//public class ExcelDataWriter {
//
//    /**
//     * Excel页签名
//     */
//    private List<String> sheetNameList = Lists.newArrayList();
//
//    /**
//     * Excel页签对应的数据类型
//     */
//    private List<Class> sheetClassList = Lists.newArrayList();
//
//    /**
//     * Excel页签对应的数据集合
//     */
//    private List<List<?>> sheetDataList = Lists.newArrayList();
//
//    /**
//     * 增加导出数据页签
//     *
//     * @param clazz     Excel页签对应的数据类型
//     * @param data      Excel页签对应的数据集合
//     * @param sheetName Excel页签名
//     */
//    public <T> ExcelDataWriter addSheet(Class<T> clazz, List<T> data, String sheetName) {
//        sheetDataList.add(data);
//        sheetClassList.add(clazz);
//        sheetNameList.add(sheetName);
//        return this;
//    }
//
//    /**
//     * 增加导出数据页签
//     *
//     * @param clazz Excel页签对应的数据类型
//     * @param data  Excel页签对应的数据集合
//     */
//    public <T> ExcelDataWriter addSheet(Class<T> clazz, List<T> data) {
//        return addSheet(clazz, data, String.format("sheet%s", sheetNameList.size() + 1));
//    }
//
//    public byte[] writeExcel() {
//        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
//            ExcelWriter writer = new ExcelWriter(outputStream, ExcelTypeEnum.XLSX);
//            int index = 0;
//            for (List<?> sheetData : sheetDataList) {
//                String sheetName = sheetNameList.get(index);
//                Class<?> clazz = sheetClassList.get(index);
//                index++;
//                Sheet sheet = new Sheet(index);
//                sheet.setSheetName(sheetName);
//                List<ExcelColumnProperty> columnPropertyList = initColumnProperty(clazz);
//                //获取数据
//                List<List<String>> data = new ArrayList<>();
//                for (Object object : sheetData) {
//                    BeanMap beanMap = BeanMap.create(object);
//                    List<String> item = new ArrayList<>();
//                    for (ExcelColumnProperty excelColumnProperty : columnPropertyList) {
//                        String cellValue = TypeUtil.getFieldStringValue(beanMap, excelColumnProperty.getField().getName(), excelColumnProperty.getFormat());
//                        item.add(cellValue);
//                    }
//                    data.add(item);
//                }
//                //构造表头
//                List<List<String>> h = new ArrayList<>();
//                int size = 0;
//                int trows = head.getRowNum();
//                for (int i = 0; i < trows; i++) {
//                    h.add(head.getHeadByRowNum(i));
//                    if (i == 0) {
//                        size = head.getHeadByRowNum(i).size();
//                    }
//                }
//                List<List<String>> v = new ArrayList<>();
//                for (int i = 0; i < size; i++) {
//                    v.add(new ArrayList<>(trows));
//                }
//                for (int i = 0; i < size; i++) {
//                    for (int j = 0; j < trows; j++) {
//                        v.get(i).add(h.get(j).get(i));
//                    }
//                }
//
//                Table table = new Table(z);
//                table.setHead(v);
//
//                //以数组方式生成数据
//                writer.write0(data, sheet, table);
//            }
//            return outputStream.toByteArray();
//        } catch (IOException e) {
//            throw new RuntimeException("生成Excel文件失败", e);
//        }
//    }
//
//
//    private List<ExcelColumnProperty> initColumnProperty(Class<?> clazz) {
//        List<ExcelColumnProperty> columnPropertyList = new ArrayList<>();
//        Field[] fields = clazz.getDeclaredFields();
//        for (Field field : fields) {
//            ExcelProperty excelProperty = field.getAnnotation(ExcelProperty.class);
//            ExcelColumnProperty excelHeadProperty = null;
//            if (excelProperty != null) {
//                excelHeadProperty = new ExcelColumnProperty();
//                excelHeadProperty.setField(field);
//                excelHeadProperty.setHead(Arrays.asList(excelProperty.value()));
//                excelHeadProperty.setIndex(excelProperty.index());
//                excelHeadProperty.setFormat(excelProperty.format());
//            }
//            if (excelHeadProperty == null) {
//                ExcelColumnNum columnNum = field.getAnnotation(ExcelColumnNum.class);
//                if (columnNum != null) {
//                    excelHeadProperty = new ExcelColumnProperty();
//                    excelHeadProperty.setField(field);
//                    excelHeadProperty.setIndex(columnNum.value());
//                    excelHeadProperty.setFormat(columnNum.format());
//                }
//            }
//            if (excelHeadProperty != null) {
//                columnPropertyList.add(excelHeadProperty);
//            }
//        }
//        return columnPropertyList;
//    }
//
//
//    public void export(HttpServletResponse response) {
//        response.setContentType("application/vnd.ms-excel;charset=utf-8");
//        response.setHeader("Content-Disposition", "attachment;filename=" + new String(("file.xlsx").getBytes(), "iso-8859-1"));
//        try (OutputStream outputStream = response.getOutputStream()) {
//
//            ExcelWriter writer = new ExcelWriter(outputStream, ExcelTypeEnum.XLSX);
//
//            var z = 0;
//            for (val dataBean : sheetDataList) {
//                //建立Sheet
//                Sheet sheet = new Sheet(z + 1, 0);
//                if (!Strings.isNullOrEmpty(sheetNameList.get(z))) {
//                    sheet.setSheetName(sheetNameList.get(z));
//                } else {
//                    sheet.setSheetName("Sheet" + (z + 1));
//                }
//
//                //获取表头注解
//                ExcelHeadProperty2 head = new ExcelHeadProperty2<>(sheetClassList.get(z));
//
//                //获取数据
//                List<List<String>> data = new ArrayList<>();
//                for (Object d : dataBean) {
//                    BeanMap beanMap = BeanMap.create(d);
//
//                    List<String> item = new ArrayList<>();
//                    for (Object obj : head.getColumnPropertyList()) {
//                        ExcelColumnProperty excelHeadProperty = (ExcelColumnProperty) obj;
//                        String cellValue = TypeUtil.getFieldStringValue(beanMap, excelHeadProperty.getField().getName(),
//                                excelHeadProperty.getFormat());
//                        item.add(cellValue);
//                    }
//                    data.add(item);
//                }
//
//                //构造表头
//                List<List<String>> h = Lists.newArrayList();
//                int size = 0;
//                int trows = head.getRowNum();
//                for (int i = 0; i < trows; i++) {
//                    h.add(head.getHeadByRowNum(i));
//                    if (i == 0) {
//                        size = head.getHeadByRowNum(i).size();
//                    }
//                }
//                List<List<String>> v = Lists.newArrayList();
//                for (int i = 0; i < size; i++) {
//                    v.add(new ArrayList<>(trows));
//                }
//                for (int i = 0; i < size; i++) {
//                    for (int j = 0; j < trows; j++) {
//                        v.get(i).add(h.get(j).get(i));
//                    }
//                }
//
//                Table table = new Table(z);
//                table.setHead(v);
//
//                //以数组方式生成数据
//                writer.write0(data, sheet, table);
//                z++;
//            }
//
//            writer.finish();
//        }
//    }
//}
