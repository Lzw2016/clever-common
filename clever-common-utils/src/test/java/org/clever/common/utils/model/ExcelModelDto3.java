package org.clever.common.utils.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class ExcelModelDto3 extends BaseRowModel implements Serializable {

    @ExcelProperty(value = {"第1列"})
    @NotBlank
    private String column1;

    @ExcelProperty(value = {"第2列"})
    @NotBlank
    private String column2;

    @ExcelProperty(value = {"第3列"})
    @NotBlank
    private String column3;

    @ExcelProperty(value = {"第4列"})
    @NotBlank
    private String column4;

    @ExcelProperty(value = {"第5列"})
    @NotBlank
    private String column5;

    @ExcelProperty(value = {"第6列"})
    @NotBlank
    private String column6;

    @ExcelProperty(value = {"第7列"})
    @NotBlank
    private String column7;

    @ExcelProperty(value = {"第8列"})
    @NotBlank
    private String column8;

    @ExcelProperty(value = {"第9列"})
    @NotBlank
    private String column9;
}