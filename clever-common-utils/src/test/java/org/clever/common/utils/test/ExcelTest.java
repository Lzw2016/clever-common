package org.clever.common.utils.test;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.clever.common.utils.excel.ExcelDataReader;
import org.junit.Test;

import javax.validation.constraints.NotBlank;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

@Slf4j
public class ExcelTest {

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class ExcelModelDto extends BaseRowModel implements Serializable {

        @ExcelProperty(value = {"第1列"}, index = 0)
        @NotBlank
        private String column1;

        @ExcelProperty(value = {"第2列"}, index = 1)
        @NotBlank
        private String column2;

        @ExcelProperty(value = {"第3列"}, index = 2)
        @NotBlank
        private String column3;

        @ExcelProperty(value = {"第4列"}, index = 3)
        @NotBlank
        private String column4;

        @ExcelProperty(value = {"第5列"}, index = 4)
        @NotBlank
        private String column5;

        @ExcelProperty(value = {"第6列"}, index = 5)
        @NotBlank
        private String column6;

        @ExcelProperty(value = {"第7列"}, index = 6)
        @NotBlank
        private String column7;

        @ExcelProperty(value = {"第8列"}, index = 7)
        @NotBlank
        private String column8;

        @ExcelProperty(value = {"第9列"}, index = 8)
        @NotBlank
        private String column9;
    }

    @Test
    public void t01() throws IOException {
        ExcelDataReader<ExcelModelDto> excelDataReader = new ExcelDataReader<>("", FileUtils.openInputStream(new File("C:\\Users\\lzw\\Downloads\\数据导出.xlsx")), ExcelModelDto.class, 200);
        excelDataReader.readExcel(1, (data, excelRow) -> {
            log.info("Excel数据 -> {}", data);
        });
        log.info("===> {}", excelDataReader.getExcelData().getClazz());
    }
}
