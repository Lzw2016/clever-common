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
    ENGLISH = "ENGLISH",
    CHINESE = "CHINESE",
    SIMPLIFIED_CHINESE = "SIMPLIFIED_CHINESE",
    TRADITIONAL_CHINESE = "TRADITIONAL_CHINESE",
}

class ExcelReaderConfig {

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
}

interface ExcelUtils {

}
























