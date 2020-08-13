interface Class {

}

interface CellData {

}

interface ExcelContentProperty {

}

interface GlobalConfiguration {

}

interface Converter<T> {
    supportJavaTypeKey(): Class;

    supportExcelTypeKey(): CellDataTypeEnum;

    convertToJavaData(cellData: CellData, contentProperty: ExcelContentProperty, globalConfiguration: GlobalConfiguration): T;

    convertToExcelData(value: T, contentProperty: ExcelContentProperty, globalConfiguration: GlobalConfiguration): CellData;
}

enum CellExtraTypeEnum {
    /** 批注信息 */
    COMMENT = "COMMENT",
    /** 超链接 */
    HYPERLINK = "HYPERLINK",
    /** 合并单元格 */
    MERGE = "MERGE",
}

enum CellDataTypeEnum {
    /** 字符串 */
    STRING = "STRING",
    /** 不需要在sharedStrings.xml，它只用于过度使用，并且数据将存储为字符串 */
    DIRECT_STRING = "DIRECT_STRING",
    /** 数字 */
    NUMBER = "NUMBER",
    /** boolean值 */
    BOOLEAN = "BOOLEAN",
    /** 空单元格 */
    EMPTY = "EMPTY",
    /** 错误格 */
    ERROR = "ERROR",
    /** 当前仅在写入时支持图像 */
    IMAGE = "IMAGE",
}

enum Locale {
    /** 英语 */
    ENGLISH = "ENGLISH",
    /** 中文 */
    CHINESE = "CHINESE",
    /** 简体中文 */
    SIMPLIFIED_CHINESE = "SIMPLIFIED_CHINESE",
    /** 繁体中文 */
    TRADITIONAL_CHINESE = "TRADITIONAL_CHINESE",
}

interface AbstractParameterBuilder<T extends AbstractParameterBuilder<T>> {

    use1904windowing(use1904windowing: boolean): T;

    locale(locale: Locale);

    autoTrim(autoTrim: boolean);

    // registerConverter<M>(converter: Converter<M>);
}

interface AbstractExcelReaderParameterBuilder<T extends AbstractExcelReaderParameterBuilder<T>> extends AbstractParameterBuilder<T> {

    headRowNumber(headRowNumber: number): T;

    useScientificFormat(useScientificFormat: boolean): T;

    // registerReadListener(readListener: ReadListener): T;
}

interface ExcelReaderSheetBuilder extends AbstractExcelReaderParameterBuilder<ExcelReaderSheetBuilder> {

    sheetNo(sheetNo: number): ExcelReaderSheetBuilder;

    sheetName(sheetName: string): ExcelReaderSheetBuilder;

    /**
     * 开始读取Excel数据(推荐)
     */
    doRead(): void;

    // /**
    //  * 读取Excel数据，并返回所有结果(数据量大时，会消耗大量内存)
    //  */
    // doReadSync(): List<T>;
}

interface ExcelReaderBuilder extends AbstractExcelReaderParameterBuilder<ExcelReaderBuilder> {
    // file(inputStream: InputStream): ExcelReaderBuilder;

    mandatoryUseInputStream(mandatoryUseInputStream: boolean): ExcelReaderBuilder;

    autoCloseStream(autoCloseStream: boolean): ExcelReaderBuilder;

    ignoreEmptyRow(ignoreEmptyRow: boolean): ExcelReaderBuilder;

    customObject(customObject: any): ExcelReaderBuilder;

    password(password: string): ExcelReaderBuilder;

    extraRead(extraRead: CellExtraTypeEnum): ExcelReaderBuilder;

    /**
     * 设置读取的页签
     * @param sheetNo 页签编号(从0开始)
     */
    sheet(sheetNo: number): ExcelReaderSheetBuilder;

    /**
     * 设置读取的页签
     * @param sheetName 页签名称(xlsx格式才支持)
     */
    sheet(sheetName: string): ExcelReaderSheetBuilder;

    doReadAll(): void;

    // doReadAllSync(): List<T>;
}

interface ExcelProperty {
    heads?: string | string[];

    index?: number;

    order?: number;

    // converter
}

interface DateTimeFormat {
    value: string;

    use1904windowing?: boolean;
}

interface NumberFormat {
    value: string;

    // roundingMode?: RoundingMode;
}

interface ExcelReaderHeadConfig extends ExcelProperty {
    dateTimeFormat?: DateTimeFormat;

    numberFormat?: NumberFormat;
}

class ExcelReaderConfig<T extends object> {

    // file?: InputStream;

    autoCloseStream: boolean = false;

    extraRead: CellExtraTypeEnum[] = [];

    ignoreEmptyRow: boolean = false;

    mandatoryUseInputStream: boolean = false;

    password?: string;

    sheet: number | string = 0;

    headRowNumber: number = 1;

    useScientificFormat: boolean = false;

    registerConverter: any[] = [];

    use1904windowing: boolean = false;

    locale: Locale = Locale.SIMPLIFIED_CHINESE;

    autoTrim: boolean = true;

    heads?: {
        [head in keyof T]?: ExcelReaderHeadConfig;
    };
}

interface ExcelUtils {

    read<T extends object>(initConfig: ExcelReaderConfig<T>): ExcelReaderBuilder;
}

declare const excelUtils: ExcelUtils;

class Test {
    aaa: string;

    bbb: number;

    ccc: boolean;
}

const excelReaderConfig = new ExcelReaderConfig<Test>();

excelReaderConfig.heads = {
    aaa: {
        heads: "aaa",
    },
    bbb: {
        heads: "bbb",
    },
    ccc: {
        heads: "ccc",
    }
}

excelUtils.read<Test>(excelReaderConfig).sheet(0).doRead();




















