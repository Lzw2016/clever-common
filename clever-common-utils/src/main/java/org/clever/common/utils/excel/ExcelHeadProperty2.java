//package org.clever.common.utils.excel;
//
///**
// * 作者： lzw<br/>
// * 创建时间：2019-05-14 18:29 <br/>
// */
//import com.alibaba.excel.annotation.ExcelColumnNum;
//import com.alibaba.excel.annotation.ExcelProperty;
//import com.alibaba.excel.metadata.ExcelColumnProperty;
//import com.google.common.collect.Lists;
//
//import java.lang.reflect.Field;
//import java.util.*;
//
//public class ExcelHeadProperty2<T> {
//
//    private final Class<T> headClazz;
//    private final List<List<String>> head;
//    private List<ExcelColumnProperty> columnPropertyList = Lists.newArrayList();
//    private Map<Integer, ExcelColumnProperty> excelColumnPropertyMap1 = new HashMap<Integer, ExcelColumnProperty>();
//
//    public ExcelHeadProperty2(Class<T> headClazz) {
//        this.headClazz = headClazz;
//        Field[] fields = this.headClazz.getDeclaredFields();
//        List<List<String>> headList = new ArrayList<List<String>>();
//        for (Field f : fields) {
//            initOneColumnProperty(f);
//        }
//        //对列排序
//        Collections.sort(columnPropertyList);
//        for (ExcelColumnProperty excelColumnProperty : columnPropertyList) {
//            headList.add(excelColumnProperty.getHead());
//        }
//        this.head = headList;
//    }
//
//    public Class<T> getHeadClazz() {
//        return headClazz;
//    }
//
//    private void initOneColumnProperty(Field f) {
//        ExcelProperty p = f.getAnnotation(ExcelProperty.class);
//        ExcelColumnProperty excelHeadProperty = null;
//        if (p != null) {
//            excelHeadProperty = new ExcelColumnProperty();
//            excelHeadProperty.setField(f);
//            excelHeadProperty.setHead(Arrays.asList(p.value()));
//            excelHeadProperty.setIndex(p.index());
//            excelHeadProperty.setFormat(p.format());
//            excelColumnPropertyMap1.put(p.index(), excelHeadProperty);
//        } else {
//            ExcelColumnNum columnNum = f.getAnnotation(ExcelColumnNum.class);
//            if (columnNum != null) {
//                excelHeadProperty = new ExcelColumnProperty();
//                excelHeadProperty.setField(f);
//                excelHeadProperty.setIndex(columnNum.value());
//                excelHeadProperty.setFormat(columnNum.format());
//                excelColumnPropertyMap1.put(columnNum.value(), excelHeadProperty);
//            }
//        }
//        if (excelHeadProperty != null) {
//            this.columnPropertyList.add(excelHeadProperty);
//        }
//
//    }
//
//    public ExcelColumnProperty getExcelColumnProperty(int columnNum) {
//        return excelColumnPropertyMap1.get(columnNum);
//    }
//
//    public List<ExcelColumnProperty> getColumnPropertyList() {
//        return columnPropertyList;
//    }
//
//    public List<String> getHeadByRowNum(int rowNum) {
//        List<String> l = new ArrayList<>(head.size());
//        for (List<String> list : head) {
//            if (list.size() > rowNum) {
//                l.add(list.get(rowNum));
//            } else {
//                l.add(list.get(list.size() - 1));
//            }
//        }
//        return l;
//    }
//
//    public int getRowNum() {
//        int headRowNum = 0;
//        for (List<String> list : head) {
//            if (list != null && list.size() > 0) {
//                if (list.size() > headRowNum) {
//                    headRowNum = list.size();
//                }
//            }
//        }
//        return headRowNum;
//    }
//}
