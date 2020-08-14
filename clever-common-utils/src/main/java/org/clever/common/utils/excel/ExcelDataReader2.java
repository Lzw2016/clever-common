package org.clever.common.utils.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelAnalysisException;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.clever.common.utils.codec.DigestUtils;
import org.clever.common.utils.codec.EncodeDecodeUtils;
import org.clever.common.utils.excel.dto.ExcelData;
import org.clever.common.utils.excel.dto.ExcelHead;
import org.clever.common.utils.excel.dto.ExcelRow;
import org.clever.common.utils.exception.ExceptionUtils;
import org.clever.common.utils.mapper.JacksonMapper;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/08/14 11:27 <br/>
 */
@Slf4j
public class ExcelDataReader2<T> {
    /**
     * 默认最大读取数据行数
     */
    public static final int LIMIT_ROWS = 2000;
    /**
     * 上传的Excel文件名称
     */
    @Getter
    private final String filename;
    /**
     * 上传的文件数据流
     */
    private final InputStream inputStream;
    /**
     * 读取Excel文件最大行数
     */
    @Getter
    private final int limitRows;
    /**
     * Excel读取结果
     */
    @Getter
    private final LinkedHashMap<String, ExcelData<T>> excelSheetMap = new LinkedHashMap<>(1);
    /**
     * 读取Excel文件的实现
     */
    private final ExcelReaderBuilder excelReaderBuilder;
    /**
     * ExcelDate 数据读取实现
     */
    private final ExcelDateReadListener excelDateReadListener = new ExcelDateReadListener();

    /**
     * @param multipartFile 上传的文件内容
     * @param clazz         Excel解析对应的数据类型
     */
    public ExcelDataReader2(MultipartFile multipartFile, Class<T> clazz) throws IOException {
        this(multipartFile.getOriginalFilename(), multipartFile.getInputStream(), clazz, LIMIT_ROWS);
    }

    /**
     * @param multipartFile 上传的文件内容
     * @param clazz         Excel解析对应的数据类型
     * @param limitRows     读取Excel文件最大行数
     */
    public ExcelDataReader2(MultipartFile multipartFile, Class<T> clazz, int limitRows) throws IOException {
        this(multipartFile.getOriginalFilename(), multipartFile.getInputStream(), clazz, limitRows);
    }

    /**
     * @param filename    上传的文件名称
     * @param inputStream 上传的文件内容
     * @param clazz       Excel解析对应的数据类型
     */
    public ExcelDataReader2(String filename, InputStream inputStream, Class<T> clazz) {
        this(filename, inputStream, clazz, LIMIT_ROWS);
    }

    /**
     * @param filename    上传的文件名称
     * @param inputStream 上传的文件内容
     * @param clazz       Excel解析对应的数据类型
     * @param limitRows   读取Excel文件最大行数
     */
    public ExcelDataReader2(String filename, InputStream inputStream, Class<T> clazz, int limitRows) {
        Assert.hasText(filename, "参数filename不能为空");
        Assert.notNull(inputStream, "参数inputStream不能为空");
        Assert.notNull(clazz, "参数clazz不能为空");
        this.filename = filename;
        this.inputStream = inputStream;
        this.limitRows = limitRows;
        this.excelReaderBuilder = new ExcelReaderBuilder();
        this.excelReaderBuilder.head(clazz);
        init();
    }

    private void init() {
        excelReaderBuilder.file(inputStream);
        excelReaderBuilder.registerReadListener(excelDateReadListener);
        excelReaderBuilder.autoCloseStream(false);
        excelReaderBuilder.ignoreEmptyRow(false);
        excelReaderBuilder.mandatoryUseInputStream(false);
        excelReaderBuilder.useScientificFormat(false);
        excelReaderBuilder.use1904windowing(false);
        excelReaderBuilder.locale(Locale.SIMPLIFIED_CHINESE);
        excelReaderBuilder.autoTrim(true);
    }

    public ExcelReaderBuilder read() {
        return excelReaderBuilder;
    }

    /**
     * 返回第一个页签数据
     */
    public ExcelData<T> getFirstExcelData() {
        if (excelSheetMap.isEmpty()) {
            return null;
        }
        for (Map.Entry<String, ExcelData<T>> entry : excelSheetMap.entrySet()) {
            return entry.getValue();
        }
        return null;
    }

    /**
     * 根据页签编号返回页签数据
     */
    public ExcelData<T> getExcelData(int sheetNo) {
        for (Map.Entry<String, ExcelData<T>> entry : excelSheetMap.entrySet()) {
            ExcelData<T> excelData = entry.getValue();
            if (excelData != null && Objects.equals(excelData.getSheetNo(), sheetNo)) {
                return excelData;
            }
        }
        return null;
    }

    /**
     * 根据页签名称返回页签数据
     */
    public ExcelData<T> getExcelData(String sheetName) {
        for (Map.Entry<String, ExcelData<T>> entry : excelSheetMap.entrySet()) {
            ExcelData<T> excelData = entry.getValue();
            if (excelData != null && Objects.equals(excelData.getSheetName(), sheetName)) {
                return excelData;
            }
        }
        return null;
    }

    private class ExcelDateReadListener extends AnalysisEventListener<T> {
        @SuppressWarnings("unchecked")
        private ExcelData<T> getExcelData(AnalysisContext context) {
            final Integer sheetNo = context.readSheetHolder().getSheetNo();
            final String sheetName = context.readSheetHolder().getSheetName();
            final Class<T> clazz = context.readSheetHolder().getClazz();
            String key = String.format("%s-%s", sheetNo, sheetName);
            return excelSheetMap.computeIfAbsent(key, s -> new ExcelData<>(clazz, sheetName, sheetNo));
        }

        @Override
        public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
            ExcelData<T> excelData = getExcelData(context);
            if (excelData.getStartTime() == null) {
                excelData.setStartTime(System.currentTimeMillis());
            }
            for (Map.Entry<Integer, String> entry : headMap.entrySet()) {
                Integer index = entry.getKey();
                String head = entry.getValue();
                ExcelHead excelHead;
                if (excelData.getHeads().size() <= index) {
                    excelHead = new ExcelHead(index, head);
                    excelData.getHeads().add(excelHead);
                } else {
                    excelHead = excelData.getHeads().get(index);
                    excelHead.getHeads().add(head);
                }
            }
        }

        @Override
        public void invoke(T data, AnalysisContext context) {
            ExcelData<T> excelData = getExcelData(context);
            int index = context.readRowHolder().getRowIndex() + 1;
            ExcelRow<T> excelRow = new ExcelRow<>(data, index);
            // 数据签名-防重机制
            String dataJson = JacksonMapper.getInstance().toJson(data);
            excelRow.setDataSignature(EncodeDecodeUtils.encodeHex(DigestUtils.sha1(dataJson.getBytes())));
            boolean success = excelData.addRow(excelRow);
            if (!success) {
                log.info("Excel数据导入数据重复，filename={} | data={}", filename, data);
            }
            // TODO jsr303校验
        }

        @Override
        public boolean hasNext(AnalysisContext context) {
            final ExcelData<T> excelData = getExcelData(context);
            // 是否重复读取
            if (excelData.getEndTime() != null && excelData.getStartTime() != null) {
                log.info("Excel Sheet已经读取完成，当前跳过，sheet={}", excelData.getSheetName());
                return false;
            }
            // 数据是否超出限制 LIMIT_ROWS
            final int rowNum = context.readRowHolder().getRowIndex() + 1;
            final int dataRowNum = rowNum - context.currentReadHolder().excelReadHeadProperty().getHeadRowNumber();
            if (limitRows >= 0 && dataRowNum > limitRows) {
                log.info("Excel数据行超出限制：dataRowNum={} | limitRows={}", dataRowNum, limitRows);
                excelData.setInterruptByRowNum(rowNum);
                // 设置已经读取完成
                doAfterAllAnalysed(context);
                return false;
            }
            return true;
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {
            ExcelData<T> excelData = getExcelData(context);
            if (excelData.getEndTime() == null) {
                excelData.setEndTime(System.currentTimeMillis());
            }
            if (excelData.getEndTime() != null && excelData.getStartTime() != null) {
                log.info("Excel Sheet读取完成，sheet={} | 耗时：{}ms", excelData.getSheetName(), excelData.getEndTime() - excelData.getStartTime());
            }
        }
    }

    /**
     * 创建ExcelDataReader
     *
     * @param request   上传文件的请求
     * @param clazz     Excel读取对应的实体类型
     * @param limitRows 最大读取行数
     */
    public static <T> ExcelDataReader2<T> newExcelDataReader(HttpServletRequest request, Class<T> clazz, int limitRows) {
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
            return new ExcelDataReader2<>(file, clazz, limitRows);
        } catch (IOException e) {
            throw ExceptionUtils.unchecked(e);
        }
    }

//    /**
//     * 创建ExcelDataReader，并读取Excel
//     *
//     * @param request               上传文件的请求
//     * @param clazz                 Excel读取对应的实体类型
//     * @param limitRows             最大读取行数
//     * @param sheetNo               Excel也签号，从1开始
//     * @param excelRowReader        自定义的行处理
//     * @param excelCellExceptionHan 自定义的单元格读取异常处理
//     */
//    public static <T> ExcelDataReader2<T> readExcel(HttpServletRequest request, Class<T> clazz, int limitRows, int sheetNo, ExcelRowReader<T> excelRowReader, ExcelCellExceptionHand<T> excelCellExceptionHan) {
//        ExcelDataReader<T> excelDataReader = newExcelDataReader(request, clazz, limitRows);
//        excelDataReader.readExcel(sheetNo, excelRowReader, excelCellExceptionHan);
//        return excelDataReader;
//    }
//
//    /**
//     * 创建ExcelDataReader，并读取Excel
//     *
//     * @param request        上传文件的请求
//     * @param clazz          Excel读取对应的实体类型
//     * @param limitRows      最大读取行数
//     * @param excelRowReader 自定义的行处理
//     */
//    public static <T> ExcelDataReader2<T> readExcel(HttpServletRequest request, Class<T> clazz, int limitRows, ExcelRowReader<T> excelRowReader) {
//        return ExcelDataReader.readExcel(request, clazz, limitRows, 1, excelRowReader, null);
//    }
//
//    /**
//     * 创建ExcelDataReader，并读取Excel
//     *
//     * @param request               上传文件的请求
//     * @param clazz                 Excel读取对应的实体类型
//     * @param limitRows             最大读取行数
//     * @param excelCellExceptionHan 自定义的单元格读取异常处理
//     */
//    public static <T> ExcelDataReader2<T> readExcel(HttpServletRequest request, Class<T> clazz, int limitRows, ExcelCellExceptionHand<T> excelCellExceptionHan) {
//        return ExcelDataReader.readExcel(request, clazz, limitRows, 1, null, excelCellExceptionHan);
//    }
//
//    /**
//     * 创建ExcelDataReader，并读取Excel
//     *
//     * @param request   上传文件的请求
//     * @param clazz     Excel读取对应的实体类型
//     * @param limitRows 最大读取行数
//     */
//    public static <T> ExcelDataReader2<T> readExcel(HttpServletRequest request, Class<T> clazz, int limitRows) {
//        return ExcelDataReader.readExcel(request, clazz, limitRows, 1, null, null);
//    }
}
