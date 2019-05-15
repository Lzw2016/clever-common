package org.clever.common.utils.excel;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.annotation.ExcelColumnNum;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelAnalysisException;
import com.alibaba.excel.metadata.ExcelColumnProperty;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.util.TypeUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.beans.BeanMap;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.clever.common.utils.codec.DigestUtils;
import org.clever.common.utils.codec.EncodeDecodeUtils;
import org.clever.common.utils.validator.ValidatorFactoryUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;

/**
 * 作者： lzw<br/>
 * 创建时间：2019-05-13 14:05 <br/>
 */
@Slf4j
public class ExcelDataReader<T> extends AnalysisEventListener<List<String>> {

    /**
     * 创建ExcelDataReader
     *
     * @param request   上传文件的请求
     * @param clazz     Excel读取对应的实体类型
     * @param limitRows 最大读取行数
     */
    public static <T> ExcelDataReader<T> newExcelDataReader(HttpServletRequest request, Class<T> clazz, int limitRows) {
        if (!(request instanceof StandardMultipartHttpServletRequest)) {
            throw new ExcelAnalysisException("当前请求未上传文件");
        }
        StandardMultipartHttpServletRequest multipartRequest = (StandardMultipartHttpServletRequest) request;
        Map<String, MultipartFile> multipartFileMap = multipartRequest.getFileMap();
        if (multipartFileMap.size() <= 0) {
            throw new ExcelAnalysisException("当前请求未上传文件");
        }
        List<MultipartFile> multipartFileList = new ArrayList<>();
        for (Map.Entry<String, MultipartFile> entry : multipartFileMap.entrySet()) {
            String name = entry.getValue().getOriginalFilename();
            String extension = FilenameUtils.getExtension(name);
            if ("xls".equalsIgnoreCase(extension) || "xlsx".equalsIgnoreCase(extension)) {
                multipartFileList.add(entry.getValue());
            }
        }
        if (multipartFileList.size() <= 0) {
            throw new ExcelAnalysisException("当前请求未上传Excel文件");
        }
        if (multipartFileList.size() >= 2) {
            throw new ExcelAnalysisException("不支持同时导入多个Excel文件");
        }
        MultipartFile file = multipartFileList.get(0);
        return new ExcelDataReader<>(file, clazz, limitRows);
    }

    /**
     * 创建ExcelDataReader，并读取Excel
     *
     * @param request        上传文件的请求
     * @param clazz          Excel读取对应的实体类型
     * @param limitRows      最大读取行数
     * @param sheetNo        Excel也签号，从1开始
     * @param excelRowReader 自定义的行处理
     */
    public static <T> ExcelDataReader<T> readExcel(HttpServletRequest request, Class<T> clazz, int limitRows, int sheetNo, ExcelRowReader<T> excelRowReader) {
        ExcelDataReader<T> excelDataReader = newExcelDataReader(request, clazz, limitRows);
        excelDataReader.readExcel(sheetNo, excelRowReader);
        return excelDataReader;
    }

    /**
     * 创建ExcelDataReader，并读取Excel
     *
     * @param request   上传文件的请求
     * @param clazz     Excel读取对应的实体类型
     * @param limitRows 最大读取行数
     */
    public static <T> ExcelDataReader<T> readExcel(HttpServletRequest request, Class<T> clazz, int limitRows) {
        return ExcelDataReader.readExcel(request, clazz, limitRows, 1, null);
    }

    /**
     * 上传的Excel文件名称
     */
    @Getter
    private String filename;
    /**
     * 上传的文件内容
     */
    private MultipartFile multipartFile;

    /**
     * 读取Excel文件最大行数
     */
    @Getter
    private int limitRows;

    /**
     * Excel读取结果
     */
    @Getter
    private ExcelData<T> excelData;

    /**
     * 列头信息
     */
    private List<ExcelColumnProperty> columnPropertyList = new ArrayList<>();
    /**
     * 列头元数据(列index -> 列头信息)
     */
    private Map<Integer, ExcelColumnProperty> excelColumnPropertyMap = new HashMap<>();

    /**
     * 是否已经开始解析
     */
    @Getter
    private volatile boolean started = false;

    /**
     * 开始解析的时间
     */
    private Long startTime;

    /**
     * 表格头行数
     */
    private int headRowNum;

    private ExcelRowReader<T> excelRowReader;

    /**
     * @param multipartFile 上传的文件内容
     * @param clazz         Excel解析对应的数据类型
     */
    public ExcelDataReader(MultipartFile multipartFile, Class<T> clazz) {
        this(multipartFile, clazz, 2000);
    }

    /**
     * @param multipartFile 上传的文件内容
     * @param clazz         Excel解析对应的数据类型
     * @param limitRows     读取Excel文件最大行数
     */
    public ExcelDataReader(MultipartFile multipartFile, Class<T> clazz, int limitRows) {
        this.filename = multipartFile.getOriginalFilename();
        this.multipartFile = multipartFile;
        this.limitRows = limitRows;
        // 解析定义的列头元数据
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            initColumnProperty(field);
        }
        if (columnPropertyList.size() <= 0) {
            throw new ExcelAnalysisException(String.format("Excel解析对象: %s，字段未使用@ExcelProperty或者@ExcelColumnNum修饰声明与Excel的映射关系", clazz));
        }
        Collections.sort(columnPropertyList);
        this.excelData = new ExcelData<>(clazz, columnPropertyList);
    }

    private void initColumnProperty(Field field) {
        ExcelProperty excelProperty = field.getAnnotation(ExcelProperty.class);
        ExcelColumnProperty excelHeadProperty = null;
        if (excelProperty != null) {
            excelHeadProperty = new ExcelColumnProperty();
            excelHeadProperty.setField(field);
            excelHeadProperty.setHead(Arrays.asList(excelProperty.value()));
            excelHeadProperty.setIndex(excelProperty.index());
            excelHeadProperty.setFormat(excelProperty.format());
            this.excelColumnPropertyMap.put(excelProperty.index(), excelHeadProperty);
        }
        if (excelHeadProperty == null) {
            ExcelColumnNum columnNum = field.getAnnotation(ExcelColumnNum.class);
            if (columnNum != null) {
                excelHeadProperty = new ExcelColumnProperty();
                excelHeadProperty.setField(field);
                excelHeadProperty.setIndex(columnNum.value());
                excelHeadProperty.setFormat(columnNum.format());
                this.excelColumnPropertyMap.put(columnNum.value(), excelHeadProperty);
            }
        }
        if (excelHeadProperty != null) {
            this.columnPropertyList.add(excelHeadProperty);
        }
    }

    /**
     * 开始解析读取Excel
     *
     * @param sheetNo Excel也签号，从1开始
     */
    public void readExcel(int sheetNo) {
        readExcel(sheetNo, null);
    }

    /**
     * 开始解析读取Excel
     *
     * @param sheetNo        Excel也签号，从1开始
     * @param excelRowReader 自定义的行处理
     */
    public synchronized void readExcel(int sheetNo, ExcelRowReader<T> excelRowReader) {
        if (started) {
            log.warn("正在解析Excel，当前调用无效");
            return;
        }
        started = true;
        this.excelRowReader = excelRowReader;
        headRowNum = excelData.getHeadRowNum();
        try (InputStream inputStream = multipartFile.getInputStream(); BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream)) {
            EasyExcelFactory.readBySax(bufferedInputStream, new Sheet(sheetNo, headRowNum), this);
        } catch (IOException e) {
            throw new ExcelAnalysisException("读取Excel文件失败", e);
        }
    }

    /**
     * 读取Excel逻辑
     *
     * @param object  行数据
     * @param context context
     */
    @Override
    public void invoke(List<String> object, AnalysisContext context) {
        if (startTime == null) {
            startTime = System.currentTimeMillis();
        }
        if (limitRows > 0 && (context.getCurrentRowNum() + 1 - headRowNum) > limitRows) {
            throw new ExcelAnalysisException(String.format("导入数据量超限，最多只能导入%s条数据，请分多批导入", limitRows));
        }
        // 构造数据对象
        ExcelRow<T> excelRow;
        try {
            T dataRow = excelData.getClazz().newInstance();
            excelRow = new ExcelRow<>(dataRow, context.getCurrentRowNum() + 1);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ExcelAnalysisException(String.format("读取Excel失败，Excel解析对象: %s，没有默认构造函数，必须定义默认构造函数", excelData.getClazz()), e);
        }
        Map<String, Object> rowMap = new HashMap<>();
        for (int index = 0; index < object.size(); index++) {
            ExcelColumnProperty columnProperty = excelColumnPropertyMap.get(index);
            if (columnProperty == null) {
                throw new ExcelAnalysisException(String.format("解析Excel文件失败，Excel解析对象: %s，字段未使用@ExcelProperty或者@ExcelColumnNum修饰声明与Excel的映射关系", excelData.getClazz()));
            }
            String cellStr = object.get(index);
            try {
                Object value = TypeUtil.convert(cellStr, columnProperty.getField(), columnProperty.getFormat(), context.use1904WindowDate());
                if (value != null) {
                    rowMap.put(columnProperty.getField().getName(), value);
                }
            } catch (Exception e) {
                excelRow.addErrorInColumn(columnProperty.getField().getName(), String.format("读取值失败，值:%s，格式:%s，类型:%s", cellStr, columnProperty.getFormat(), columnProperty.getField().getType()));
            }
        }
        BeanMap.create(excelRow.getData()).putAll(rowMap);
        // 数据去重
        StringBuilder sb = new StringBuilder();
        object.forEach(str -> {
            if (StringUtils.isNotBlank(str)) {
                sb.append(str);
            }
        });
        if (sb.length() > 0) {
            excelRow.setDataSignature(EncodeDecodeUtils.encodeHex(DigestUtils.sha1(sb.toString().getBytes())));
        }
        boolean added = excelData.addRow(excelRow);
        if (!added) {
            return;
        }
        // 数据校验
        if (!excelRow.hasError()) {
            // jsr303校验
            Validator validator = ValidatorFactoryUtils.getHibernateValidator();
            Set<ConstraintViolation<T>> set = validator.validate(excelRow.getData());
            for (ConstraintViolation<T> constraintViolation : set) {
                excelRow.addErrorInColumn(constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage());
            }
        }
        if (!excelRow.hasError() && excelRowReader != null) {
            try {
                excelRowReader.readerRow(excelRow.getData(), excelRow);
            } catch (Throwable e) {
                excelRow.addErrorInRow(e.getMessage());
            }
        }
    }

    /**
     * 数据读取完成
     *
     * @param context context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        log.info("[Excel数据导入] 文件：{}，耗时：{}秒，Excel数据行：{}", filename, (System.currentTimeMillis() - startTime) / 1000.0, context.getCurrentRowNum());
        startTime = null;
        // log.info("CurrentRowNum: {}", context.getCurrentRowNum());
        if (excelData.getRows().size() <= 0) {
            throw new ExcelAnalysisException("导入的Excel文件没有任何数据");
        }
    }
}
