package org.clever.common.utils.test;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.clever.common.utils.excel.ExcelDataReader;
import org.clever.common.utils.model.ExcelModelDto;
import org.clever.common.utils.model.ExcelModelDto2;
import org.clever.common.utils.model.ExcelModelDto3;
import org.clever.common.utils.model.ExcelModelDto4;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@SuppressWarnings("CodeBlock2Expr")
@Slf4j
public class ExcelTest {

    // --------------------------------------------------------------------------------------------------------------------------------------------------------- 读取Excel

    private static final String filename = "G:\\SourceCode\\clever\\clever-common\\clever-common-utils\\src\\test\\resources\\数据导出.xlsx";

    /**
     * 不继承 BaseRowModel
     * 不使用 index
     */
    @Test
    public void t01() throws IOException {
        FileInputStream inputStream = FileUtils.openInputStream(new File(filename));
        ExcelDataReader<ExcelModelDto> excelDataReader = new ExcelDataReader<>("", inputStream, ExcelModelDto.class, 200);
        excelDataReader.readExcel(1, (data, excelRow) -> {
            log.info("Excel数据 -> {}", data);
        });
        log.info("===> {}", excelDataReader.getExcelData().getExcelImportState());
    }

    /**
     * 继承 BaseRowModel
     * 使用 index
     */
    @Test
    public void t02() throws IOException {
        FileInputStream inputStream = FileUtils.openInputStream(new File(filename));
        ExcelDataReader<ExcelModelDto2> excelDataReader = new ExcelDataReader<>("", inputStream, ExcelModelDto2.class, 200);
        excelDataReader.readExcel(1, (data, excelRow) -> {
            log.info("Excel数据 -> {}", data);
        });
        log.info("===> {}", excelDataReader.getExcelData().getExcelImportState());
    }

    /**
     * 继承 BaseRowModel
     * 不使用 index
     */
    @Test
    public void t03() throws IOException {
        FileInputStream inputStream = FileUtils.openInputStream(new File(filename));
        ExcelDataReader<ExcelModelDto3> excelDataReader = new ExcelDataReader<>("", inputStream, ExcelModelDto3.class, 200);
        excelDataReader.readExcel(1, (data, excelRow) -> {
            log.info("Excel数据 -> {}", data);
        });
        log.info("===> {}", excelDataReader.getExcelData().getExcelImportState());
    }

    /**
     * 不继承 BaseRowModel
     * 部分使用 index
     */
    @Test
    public void t04() throws IOException {
        FileInputStream inputStream = FileUtils.openInputStream(new File(filename));
        ExcelDataReader<ExcelModelDto4> excelDataReader = new ExcelDataReader<>("", inputStream, ExcelModelDto4.class, 200);
        excelDataReader.readExcel(1, (data, excelRow) -> {
            log.info("Excel数据 -> {}", data);
        });
        log.info("===> {}", excelDataReader.getExcelData().getExcelImportState());
    }

    // --------------------------------------------------------------------------------------------------------------------------------------------------------- 其他测试

    @SuppressWarnings({"StringBufferReplaceableByString", "ConstantConditions"})
    @Test
    public void t90() {
        StringBuilder sb = new StringBuilder();
        String sss = null;
        sb.append(sss);
        log.info("=== | {}", sb.toString());
    }
}
