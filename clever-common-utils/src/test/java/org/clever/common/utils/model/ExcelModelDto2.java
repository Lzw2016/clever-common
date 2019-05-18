package org.clever.common.utils.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 作者： lzw<br/>
 * 创建时间：2019-05-16 18:50 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ExcelModelDto2 extends BaseRowModel implements Serializable {

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
