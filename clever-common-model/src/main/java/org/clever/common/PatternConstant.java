package org.clever.common;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-11-01 21:27 <br/>
 */
public class PatternConstant {

    /**
     * 正则匹配字符 a-z、A-Z、0-9、中文、[、]、{、}、_、- <br />
     * 使用如：Name_Pattern + "{3,127}" 匹配长度3-127个字符
     */
    public static final String Name_Pattern = "[a-zA-Z0-9\\u4e00-\\u9fa5()\\[\\]{}_-]";

    /**
     * Url正则匹配
     */
    public static final String Url_Pattern = "(https?)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]";

    /**
     * 手机号正则表达式：1开始，长度11为数字
     */
    public static final String Telephone_Pattern = "1[0-9]{10}";

    /**
     * 正则匹配：非空字符串
     */
    public static final String NotBlank_Pattern = "[\\s\\S]*\\S+[\\s\\S]*";
}
