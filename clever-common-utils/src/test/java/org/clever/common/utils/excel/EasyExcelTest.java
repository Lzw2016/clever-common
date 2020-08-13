package org.clever.common.utils.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.enums.CellExtraTypeEnum;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.read.builder.ExcelReaderSheetBuilder;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/08/13 10:06 <br/>
 */
@Slf4j
public class EasyExcelTest {

    private final String file = "C:\\Users\\lizw\\Downloads\\药店积分商品统计20200813141849.xlsx";

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

        ExcelWriterSheetBuilder excelWriterSheetBuilder = new ExcelWriterSheetBuilder();
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

            @Override
            public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
                log.info("headMap -> {}", headMap);
            }

            @Override
            public void invoke(Map<Integer, CellData<?>> data, AnalysisContext context) {
                log.info("data    -> {}", data);
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

    @Data
    public static class DemoData {
        @ExcelProperty("序号")
        private Integer index;

        @ExcelProperty("药店ID")
        private Long id;

        @ExcelProperty("药店名称")
        private String name;

        @ExcelProperty("积分商品总数量")
        private BigDecimal num1;

        @ExcelProperty("上架积分商品数")
        private BigDecimal num2;

        @ExcelProperty("下架积分商品数")
        private BigDecimal num3;
    }
}






















