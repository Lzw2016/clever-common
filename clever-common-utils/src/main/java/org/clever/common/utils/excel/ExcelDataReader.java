package org.clever.common.utils.excel;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelAnalysisException;
import com.alibaba.excel.metadata.ExcelColumnProperty;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.util.TypeUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.beans.BeanMap;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.clever.common.utils.codec.DigestUtils;
import org.clever.common.utils.codec.EncodeDecodeUtils;
import org.clever.common.utils.exception.ExceptionUtils;
import org.clever.common.utils.validator.ValidatorFactoryUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;


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
        if (!(request instanceof MultipartRequest)) {
            throw new ExcelAnalysisException("当前请求未上传文件");
        }
        MultipartRequest multipartRequest = (MultipartRequest) request;
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
        try {
            return new ExcelDataReader<>(file, clazz, limitRows);
        } catch (IOException e) {
            throw ExceptionUtils.unchecked(e);
        }
    }

    /**
     * 创建ExcelDataReader，并读取Excel
     *
     * @param request               上传文件的请求
     * @param clazz                 Excel读取对应的实体类型
     * @param limitRows             最大读取行数
     * @param sheetNo               Excel也签号，从1开始
     * @param excelRowReader        自定义的行处理
     * @param excelCellExceptionHan 自定义的单元格读取异常处理
     */
    public static <T> ExcelDataReader<T> readExcel(HttpServletRequest request, Class<T> clazz, int limitRows, int sheetNo, ExcelRowReader<T> excelRowReader, ExcelCellExceptionHand<T> excelCellExceptionHan) {
        ExcelDataReader<T> excelDataReader = newExcelDataReader(request, clazz, limitRows);
        excelDataReader.readExcel(sheetNo, excelRowReader, excelCellExceptionHan);
        return excelDataReader;
    }

    /**
     * 创建ExcelDataReader，并读取Excel
     *
     * @param request        上传文件的请求
     * @param clazz          Excel读取对应的实体类型
     * @param limitRows      最大读取行数
     * @param excelRowReader 自定义的行处理
     */
    public static <T> ExcelDataReader<T> readExcel(HttpServletRequest request, Class<T> clazz, int limitRows, ExcelRowReader<T> excelRowReader) {
        return ExcelDataReader.readExcel(request, clazz, limitRows, 1, excelRowReader, null);
    }

    /**
     * 创建ExcelDataReader，并读取Excel
     *
     * @param request               上传文件的请求
     * @param clazz                 Excel读取对应的实体类型
     * @param limitRows             最大读取行数
     * @param excelCellExceptionHan 自定义的单元格读取异常处理
     */
    public static <T> ExcelDataReader<T> readExcel(HttpServletRequest request, Class<T> clazz, int limitRows, ExcelCellExceptionHand<T> excelCellExceptionHan) {
        return ExcelDataReader.readExcel(request, clazz, limitRows, 1, null, excelCellExceptionHan);
    }

    /**
     * 创建ExcelDataReader，并读取Excel
     *
     * @param request   上传文件的请求
     * @param clazz     Excel读取对应的实体类型
     * @param limitRows 最大读取行数
     */
    public static <T> ExcelDataReader<T> readExcel(HttpServletRequest request, Class<T> clazz, int limitRows) {
        return ExcelDataReader.readExcel(request, clazz, limitRows, 1, null, null);
    }

    /**
     * 默认最大读取数据行数
     */
    public static final int LIMIT_ROWS = 2000;
    /**
     * 上传的Excel文件名称
     */
    @Getter
    private String filename;
    /**
     * 上传的文件数据流
     */
    private InputStream inputStream;
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
    /**
     * 列头信息
     */
    private List<ExcelColumnProperty> columnPropertyList = new ArrayList<>();
    /**
     * 列头元数据(列index -> 列头信息)
     */
    private Map<Integer, ExcelColumnProperty> excelColumnPropertyMap1 = new HashMap<>();
    /**
     * 列头元数据(列名称 -> 列头信息)
     */
    private Map<String, ExcelColumnProperty> excelColumnPropertyMap2 = new HashMap<>();
    // /**
    //  * Excel表格头数据 - 导入Excel表格中的真实数据
    //  */
    // private List<List<String>> excelHeadRealData = new ArrayList<>();
    /**
     * Excel表格头数据(最后一行) - 导入Excel表格中的真实数据
     */
    private List<String> lastExcelHeadRealData;
    /**
     * 处理Excel行数据
     */
    private ExcelRowReader<T> excelRowReader;
    /**
     * 处理单元格异常
     */
    @Setter
    @Getter
    private ExcelCellExceptionHand<T> excelCellExceptionHand;

    /**
     * @param multipartFile 上传的文件内容
     * @param clazz         Excel解析对应的数据类型
     */
    public ExcelDataReader(MultipartFile multipartFile, Class<T> clazz) throws IOException {
        this(multipartFile.getOriginalFilename(), multipartFile.getInputStream(), clazz, LIMIT_ROWS);
    }

    /**
     * @param multipartFile 上传的文件内容
     * @param clazz         Excel解析对应的数据类型
     * @param limitRows     读取Excel文件最大行数
     */
    public ExcelDataReader(MultipartFile multipartFile, Class<T> clazz, int limitRows) throws IOException {
        this(multipartFile.getOriginalFilename(), multipartFile.getInputStream(), clazz, limitRows);
    }

    /**
     * @param inputStream 上传的文件内容
     * @param clazz       Excel解析对应的数据类型
     */
    public ExcelDataReader(InputStream inputStream, Class<T> clazz) {
        this("", inputStream, clazz, LIMIT_ROWS);
    }

    /**
     * @param filename    上传的文件名称
     * @param inputStream 上传的文件内容
     * @param clazz       Excel解析对应的数据类型
     */
    public ExcelDataReader(String filename, InputStream inputStream, Class<T> clazz) {
        this(filename, inputStream, clazz, LIMIT_ROWS);
    }

    /**
     * @param filename    上传的文件名称
     * @param inputStream 上传的文件内容
     * @param clazz       Excel解析对应的数据类型
     * @param limitRows   读取Excel文件最大行数
     */
    public ExcelDataReader(String filename, InputStream inputStream, Class<T> clazz, int limitRows) {
        this.filename = filename;
        this.inputStream = inputStream;
        this.limitRows = limitRows;
        initColumnProperty(clazz);
        this.excelData = new ExcelData<>(clazz, columnPropertyList);
    }

    /**
     * 开始解析读取Excel
     */
    public void readExcel() {
        readExcel(1, null, null);
    }

    /**
     * 开始解析读取Excel
     *
     * @param sheetNo Excel也签号，从1开始
     */
    public void readExcel(int sheetNo) {
        readExcel(sheetNo, null, null);
    }

    /**
     * 开始解析读取Excel
     *
     * @param excelRowReader 自定义的行处理
     */
    public void readExcel(ExcelRowReader<T> excelRowReader) {
        readExcel(1, excelRowReader, null);
    }

    /**
     * 开始解析读取Excel
     *
     * @param excelCellExceptionHand 自定义的单元格读取异常处理
     */
    public void readExcel(ExcelCellExceptionHand<T> excelCellExceptionHand) {
        readExcel(1, null, excelCellExceptionHand);
    }

    /**
     * 开始解析读取Excel
     *
     * @param sheetNo        Excel也签号，从1开始
     * @param excelRowReader 自定义的行处理
     */
    public void readExcel(int sheetNo, ExcelRowReader<T> excelRowReader) {
        readExcel(sheetNo, excelRowReader, null);
    }

    /**
     * 开始解析读取Excel
     *
     * @param sheetNo                Excel也签号，从1开始
     * @param excelCellExceptionHand 自定义的单元格读取异常处理
     */
    public void readExcel(int sheetNo, ExcelCellExceptionHand<T> excelCellExceptionHand) {
        readExcel(sheetNo, null, excelCellExceptionHand);
    }

    /**
     * 开始解析读取Excel
     *
     * @param sheetNo                Excel也签号，从1开始
     * @param excelRowReader         自定义的行处理
     * @param excelCellExceptionHand 自定义的单元格读取异常处理
     */
    public synchronized void readExcel(int sheetNo, ExcelRowReader<T> excelRowReader, ExcelCellExceptionHand<T> excelCellExceptionHand) {
        if (started) {
            log.warn("正在解析Excel，当前调用无效");
            return;
        }
        started = true;
        this.excelRowReader = excelRowReader;
        this.excelCellExceptionHand = excelCellExceptionHand;
        headRowNum = excelData.getHeadRowNum();
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream)) {
            EasyExcelFactory.readBySax(bufferedInputStream, new Sheet(sheetNo, 0), this);
        } catch (IOException e) {
            throw new ExcelAnalysisException("读取Excel文件失败", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    /**
     * 读取Excel逻辑
     *
     * @param object  行数据
     * @param context context
     */
    @Override
    public synchronized void invoke(List<String> object, AnalysisContext context) {
        final int currentRowNum = context.getCurrentRowNum() + 1;
        // 解析初始化 - 开始解析时执行一次
        if (startTime == null) {
            startTime = System.currentTimeMillis();
            excelData.clearData();
        }
        // 当前行是表格头
        if (currentRowNum <= headRowNum) {
            lastExcelHeadRealData = object.stream().map(StringUtils::trim).collect(Collectors.toList());
            // excelHeadRealData.add(lastExcelHeadRealData);
            return;
        }
        // 超出解析数据最大行数
        if (limitRows > 0 && (currentRowNum - headRowNum) > limitRows) {
            throw new ExcelAnalysisException(String.format("导入数据量超限，最多只能导入%s条数据，请分多批导入", limitRows));
        }
        // 构造数据对象
        ExcelRow<T> excelRow;
        try {
            T dataRow = excelData.getClazz().newInstance();
            excelRow = new ExcelRow<>(dataRow, currentRowNum);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ExcelAnalysisException(String.format("读取Excel失败，Excel解析对象: %s，没有默认构造函数，必须定义默认构造函数", excelData.getClazz()), e);
        }
        boolean readRowSuccess = false;
        Map<String, Object> rowMap = new HashMap<>();
        for (int index = 0; index < object.size(); index++) {
            // 先读取index对应的 ExcelColumnProperty
            ExcelColumnProperty columnProperty = excelColumnPropertyMap1.get(index);
            if (columnProperty == null && lastExcelHeadRealData != null && lastExcelHeadRealData.size() > 0) {
                // 根据Excel表格头读取对应的 ExcelColumnProperty
                columnProperty = excelColumnPropertyMap2.get(StringUtils.trim(lastExcelHeadRealData.get(index)));
            }
            if (columnProperty == null && columnPropertyList.size() == object.size()) {
                // 最后只能根据Excel单元格位置读取对应的 ExcelColumnProperty
                columnProperty = columnPropertyList.get(index);
            }
            if (columnProperty == null) {
                continue;
            }
            readRowSuccess = true;
            String cellStr = StringUtils.trim(object.get(index));
            try {
                Object value = TypeUtil.convert(cellStr, columnProperty.getField(), columnProperty.getFormat(), context.use1904WindowDate());
                if (value != null) {
                    rowMap.put(columnProperty.getField().getName(), value);
                }
            } catch (Throwable e) {
                if (excelCellExceptionHand != null) {
                    try {
                        excelCellExceptionHand.exceptionHand(e, cellStr, object, excelRow, columnProperty);
                    } catch (Throwable e2) {
                        excelRow.addErrorInColumn(columnProperty.getField().getName(), e2.getMessage());
                    }
                } else {
                    excelRow.addErrorInColumn(columnProperty.getField().getName(), String.format("读取值失败，值:%s，格式:%s，类型:%s", cellStr, columnProperty.getFormat(), columnProperty.getField().getType()));
                }
            }
        }
        if (!readRowSuccess) {
            throw new ExcelAnalysisException(String.format("解析Excel文件失败，Excel解析对象: %s，@ExcelProperty或者@ExcelColumnNum修饰字段是必须使用index属性", excelData.getClazz()));
        }
        BeanMap.create(excelRow.getData()).putAll(rowMap);
        // 数据去重
        StringBuilder sb = new StringBuilder();
        for (int index = 0; index < object.size(); index++) {
            sb.append(index).append('=').append(object.get(index)).append('|');
        }
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
        long time = 0;
        if (startTime != null) {
            time = System.currentTimeMillis() - startTime;
        }
        log.info("[Excel数据导入] 文件：{}，耗时：{}秒，Excel数据行：{}", filename, time / 1000.0, context.getCurrentRowNum());
        startTime = null;
        if (excelData.getRows().size() <= 0) {
            throw new ExcelAnalysisException("导入的Excel文件没有任何数据");
        }
    }

    /**
     * 读取Excel列信息
     *
     * @see com.alibaba.excel.metadata.ExcelHeadProperty
     * @see com.alibaba.excel.modelbuild.ModelBuildEventListener
     * @see com.alibaba.excel.analysis.BaseSaxAnalyser
     */
    private void initColumnProperty(Class<T> clazz) {
        int useIndexNum = 0;
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            ExcelColumnProperty excelHeadProperty = InternalUtils.getExcelColumnProperty(field);
            if (excelHeadProperty == null) {
                continue;
            }
            excelColumnPropertyMap1.put(excelHeadProperty.getIndex(), excelHeadProperty);
            if (excelHeadProperty.getIndex() < 99999) {
                useIndexNum++;
            }
            List<String> head = excelHeadProperty.getHead();
            if (head != null && head.size() > 0) {
                excelColumnPropertyMap2.put(head.get(head.size() - 1), excelHeadProperty);
            }
            columnPropertyList.add(excelHeadProperty);
        }
        if (columnPropertyList.size() <= 0) {
            throw new ExcelAnalysisException(String.format("Excel解析对象: %s，字段未使用@ExcelProperty或者@ExcelColumnNum修饰声明与Excel的映射关系", clazz));
        }
        Collections.sort(columnPropertyList);
        if (useIndexNum < columnPropertyList.size()) {
            log.warn("Excel解析对象: {}，@ExcelProperty或者@ExcelColumnNum修饰字段只有部分使用了index属性", clazz);
        }
    }
}
