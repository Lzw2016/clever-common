package org.clever.common.utils.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.enums.CellExtraTypeEnum;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.read.builder.ExcelReaderSheetBuilder;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/08/13 10:06 <br/>
 */
@Slf4j
public class EasyExcelTest {

    private final String file = "C:\\Users\\lizw\\Downloads\\药店积分商品统计20200813141849.xlsx";

    private final String file2 = "C:\\Users\\lizw\\Downloads\\药店积分商品统计20200813141850.xlsx";

    @Test
    public void t01() {
        ExcelReaderBuilder excelReaderBuilder = new ExcelReaderBuilder();

        excelReaderBuilder.file("");
        excelReaderBuilder.autoCloseStream(true);
        excelReaderBuilder.customObject(new Object());
        excelReaderBuilder.excelType(ExcelTypeEnum.XLSX);
        excelReaderBuilder.extraRead(CellExtraTypeEnum.COMMENT);
        excelReaderBuilder.ignoreEmptyRow(true);
        excelReaderBuilder.mandatoryUseInputStream(true);
        excelReaderBuilder.password("");
        excelReaderBuilder.sheet(0);
        excelReaderBuilder.useDefaultListener(true);

        excelReaderBuilder.readCache(null);
        excelReaderBuilder.readCacheSelector(null);

        excelReaderBuilder.doReadAll();
        excelReaderBuilder.doReadAllSync();

        excelReaderBuilder.headRowNumber(1);
        excelReaderBuilder.useScientificFormat(false);
        excelReaderBuilder.registerReadListener(null);
        excelReaderBuilder.head(new ArrayList<>());
        excelReaderBuilder.registerConverter(null);
        excelReaderBuilder.use1904windowing(false);
        excelReaderBuilder.locale(Locale.SIMPLIFIED_CHINESE);
        excelReaderBuilder.autoTrim(true);

        ExcelReader excelReader = excelReaderBuilder.build();
        excelReader.excelExecutor().execute();

        ExcelReaderSheetBuilder excelReaderSheetBuilder = new ExcelReaderSheetBuilder();
        excelReaderSheetBuilder.build();
    }

    @Test
    public void t02() {
        ExcelWriterBuilder excelWriterBuilder = new ExcelWriterBuilder();
        excelWriterBuilder.file("");
        excelWriterBuilder.autoCloseStream(true);
        excelWriterBuilder.inMemory(false);
        excelWriterBuilder.withTemplate("");
        excelWriterBuilder.writeExcelOnException(false);
        excelWriterBuilder.automaticMergeHead(true);
        excelWriterBuilder.excludeColumnFiledNames(null);
        excelWriterBuilder.excludeColumnIndexes(null);
        excelWriterBuilder.includeColumnFiledNames(null);
        excelWriterBuilder.includeColumnIndexes(null);
        excelWriterBuilder.needHead(true);
        excelWriterBuilder.registerConverter(null);
        //excelWriterBuilder.registerWriteHandler(null);
        excelWriterBuilder.relativeHeadRowIndex(0);
        excelWriterBuilder.useDefaultStyle(true);
        excelWriterBuilder.excelType(ExcelTypeEnum.XLSX);
        excelWriterBuilder.password("");
        excelWriterBuilder.sheet(0);
        excelWriterBuilder.head(new ArrayList<>());
        excelWriterBuilder.use1904windowing(false);
        excelWriterBuilder.locale(Locale.SIMPLIFIED_CHINESE);
        excelWriterBuilder.autoTrim(true);

        ExcelWriter excelWriter = excelWriterBuilder.build();
//        excelWriter.write()
//        excelWriter.fill()
//        excelWriter.writeContext()
        excelWriter.finish();

        ExcelWriterSheetBuilder excelWriterSheetBuilder = excelWriterBuilder.sheet(0);
//        excelWriterSheetBuilder.doWrite();
//        excelWriterSheetBuilder.doFill();
//        excelWriterSheetBuilder.sheetNo()
//        excelWriterSheetBuilder.sheetName()
//        excelWriterSheetBuilder.table();
    }

    @Test
    public void t03() {
//        EasyExcel.read().sheet().doRead();
        ExcelReaderBuilder excelReaderBuilder = new ExcelReaderBuilder();
//        excelReaderBuilder.file("C:\\Users\\lizw\\Downloads\\研发一部3月考勤.xls");
        excelReaderBuilder.file(file);
        List<List<String>> heads = new ArrayList<>();
//        heads.add(Arrays.asList("序号", "药店ID", "药店名称", "积分商品总数量", "上架积分商品数", "下架积分商品数"));
        heads.add(Collections.singletonList("序号"));
        heads.add(Collections.singletonList("药店ID"));
        heads.add(Collections.singletonList("药店名称"));
        heads.add(Collections.singletonList("积分商品总数量"));
        heads.add(Collections.singletonList("上架积分商品数"));
        heads.add(Collections.singletonList("下架积分商品数"));
//        excelReaderBuilder.head(heads);
//        excelReaderBuilder.headRowNumber(1);
        excelReaderBuilder.registerReadListener(new AnalysisEventListener<Map<Integer, CellData<?>>>() {

            private Map<Integer, String> headMap;

            @Override
            public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
                this.headMap = headMap;
                log.info("headMap -> {}", headMap);
            }

            @Override
            public void invoke(Map<Integer, CellData<?>> data, AnalysisContext context) {
                Map<String, CellData<?>> row = new LinkedHashMap<>(data.size());
                for (Map.Entry<Integer, CellData<?>> entry : data.entrySet()) {
                    row.put(headMap.get(entry.getKey()), entry.getValue());
                }
//                log.info("data    -> {}", data);
                log.info("row     -> {}", row);
//                ConverterUtils.convertToJavaObject(
//                        cellData,
//                        excelContentProperty.getField(),
//                        excelContentProperty,
//                        currentReadHolder.converterMap(),
//                        currentReadHolder.globalConfiguration(),
//                        context.readRowHolder().getRowIndex(),
//                        index);
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext context) {
                log.info("所有数据解析完成！");
            }
        });

        excelReaderBuilder.sheet(0).doRead();
        excelReaderBuilder.sheet(0).doRead();
    }

    @Test
    public void t04() {
        EasyExcel.read(file, DemoData.class, new AnalysisEventListener<DemoData>() {
            @Override
            public void invoke(DemoData data, AnalysisContext context) {
                log.info("data    -> {}", data);
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext context) {
                log.info("所有数据解析完成！");
            }
        }).sheet().doRead();
    }

    @Test
    public void t05() throws IOException {
        FileInputStream inputStream = FileUtils.openInputStream(new File(file));
        ExcelDataReader<DemoData> excelDataReader = new ExcelDataReader<>("test", inputStream, DemoData.class);
//        excelDataReader.setEnableValidation(false);
        excelDataReader.read().file(file).autoCloseStream(false);
        excelDataReader.read().doReadAll();
        log.info("data -> {}", excelDataReader.getExcelData(0));
        excelDataReader.read().doReadAll();
        inputStream.close();

        // ExcelDataReader.read(null, DemoData.class).sheet(0).doRead();
    }

    @Test
    public void t06() {
        List<List<String>> heads = new ArrayList<>();
//        heads.add(Arrays.asList("序号", "药店ID", "药店名称", "积分商品总数量", "上架积分商品数", "下架积分商品数"));
        heads.add(new ArrayList<>(Arrays.asList("第一", "序号")));
        heads.add(new ArrayList<>(Arrays.asList("第一", "药店ID")));
        heads.add(new ArrayList<>(Arrays.asList("第一", "药店名称")));
        heads.add(new ArrayList<>(Arrays.asList("第二", "积分商品总数量")));
        heads.add(new ArrayList<>(Arrays.asList("第二", "上架积分商品数")));
        heads.add(new ArrayList<>(Arrays.asList("第二", "下架积分商品数")));

        List<DemoData> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            DemoData demoData = new DemoData();
            demoData.index = i;
            demoData.id = (long) i;
            demoData.name = "药店名称" + i;
            demoData.num1 = new BigDecimal(i + 10001);
            demoData.num2 = new BigDecimal(i + 2000001);
            demoData.num3 = new BigDecimal(i + 300000001);
            list.add(demoData);
        }

        ExcelWriterBuilder excelWriterBuilder = new ExcelWriterBuilder();
        excelWriterBuilder.file(file2);
        excelWriterBuilder.head(heads);
//        excelWriterBuilder.head(DemoData.class);
//        excelWriterBuilder.relativeHeadRowIndex(3);
        excelWriterBuilder.registerWriteHandler(new LongestMatchColumnWidthStyleStrategy());
        excelWriterBuilder.sheet(0).doWrite(list);
    }

    @Test
    public void t07() throws IOException {
        List<DemoData> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            DemoData demoData = new DemoData();
            demoData.index = i;
            demoData.id = (long) i;
            demoData.name = "AA药店名称" + i;
            demoData.num1 = new BigDecimal(i + 66001);
            demoData.num2 = new BigDecimal(i + 8800001);
            demoData.num3 = new BigDecimal(i + 990000001);
            list.add(demoData);
        }
        FileOutputStream outputStream = FileUtils.openOutputStream(new File(file2));
        ExcelWriterBuilder builder = ExcelDataWriter.write(outputStream, DemoData.class);

        ExcelWriter excelWriter = builder.build();
        WriteSheet writeSheet = EasyExcel.writerSheet(0).needHead(Boolean.TRUE).build();
        excelWriter.write(list, writeSheet);
        excelWriter.write(list, writeSheet);
        excelWriter.write(list, writeSheet);
        excelWriter.finish();

//        ExcelWriterSheetBuilder sheetBuilder = builder.sheet(1);
//        sheetBuilder.doWrite(list);
        outputStream.close();
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void t08() throws IOException {
        FileInputStream inputStream = FileUtils.openInputStream(new File(file));
        ExcelDataReader<Map> excelDataReader = new ExcelDataReader<>("test", inputStream, Map.class);
        excelDataReader.read().file(file).autoCloseStream(false);
        excelDataReader.read().doReadAll();
        log.info("data -> {}", excelDataReader.getExcelData(0));
        excelDataReader.read().doReadAll();
        inputStream.close();

        // ExcelDataReader.read(null, DemoData.class).sheet(0).doRead();
    }

    @Test
    public void t09() throws IOException {
        List<List<String>> heads = new ArrayList<>();
//        heads.add(Arrays.asList("序号", "药店ID", "药店名称", "积分商品总数量", "上架积分商品数", "下架积分商品数"));
        heads.add(new ArrayList<>(Arrays.asList("第一", "序号")));
        heads.add(new ArrayList<>(Arrays.asList("第一", "药店ID")));
        heads.add(new ArrayList<>(Arrays.asList("第一", "药店名称")));
        heads.add(new ArrayList<>(Arrays.asList("第二", "积分商品总数量")));
        heads.add(new ArrayList<>(Arrays.asList("第二", "上架积分商品数")));
        heads.add(new ArrayList<>(Arrays.asList("第二", "下架积分商品数")));

        List<List<Object>> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            List<Object> data = new ArrayList<>();
            data.add("字符串" + i);
            data.add(new Date());
            data.add(0.56);
            data.add("111");
            data.add("222");
            data.add("333");
            list.add(data);
        }

        FileOutputStream outputStream = FileUtils.openOutputStream(new File(file2));
        ExcelWriterBuilder builder = ExcelDataWriter.write(outputStream, null);

        ExcelWriter excelWriter = builder.head(heads).build();
        WriteSheet writeSheet = EasyExcel.writerSheet(0).needHead(Boolean.TRUE).build();
        excelWriter.write(list, writeSheet);
        excelWriter.write(list, writeSheet);
        excelWriter.write(list, writeSheet);
        excelWriter.finish();

//        ExcelWriterSheetBuilder sheetBuilder = builder.sheet(1);
//        sheetBuilder.doWrite(list);
        outputStream.close();
    }

    @Data
    public static class DemoData {
        @ColumnWidth(6)
        @ExcelProperty("序号")
        private Integer index;

        @NumberFormat("###,##0.00")
        @ColumnWidth(10)
        @ExcelProperty(value = "药店ID", order = 0)
        private Long id;

        @ColumnWidth(14)
        @ExcelProperty("药店名称")
        private String name;

        @ColumnWidth(21)
        @ExcelProperty("积分商品总数量")
        private BigDecimal num1;

        @ColumnWidth(21)
        @ExcelProperty("上架积分商品数")
        private BigDecimal num2;

        @ColumnWidth(21)
        @ExcelProperty("下架积分商品数")
        private BigDecimal num3;
    }
}






















