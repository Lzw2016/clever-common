package org.clever.common.utils;


import org.apache.commons.text.StringEscapeUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串操作类工具，继承org.apache.commons.lang3.StringUtils<br/>
 * 作者：LiZW <br/>
 * 创建时间：2016-4-28 0:20 <br/>
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {

    public static final String EMPTY_JSON = "{}";
    public static final char C_BACKSLASH = '\\';
    public static final char C_DELIM_START = '{';

    /**
     * 调用对象的toString方法，如果对象为空返回默认值
     *
     * @param object     需要toString的对象
     * @param defaultStr 对象为空时返回的默认值
     * @return 返回对象的toString方法结果
     */
    public String objectToString(Object object, String defaultStr) {
        if (null == object) {
            return defaultStr;
        } else {
            return object.toString();
        }
    }

    /**
     * 除去html标签
     *
     * @param htmlStr 含有html标签的字符串
     * @return 网页文本内容
     */
    public static String delHTMLTag(String htmlStr) {
        if (isBlank(htmlStr)) {
            return htmlStr;
        }
        htmlStr = StringEscapeUtils.unescapeHtml4(htmlStr);
        //定义script的正则表达式
        String regEx_script = "<script[^>]*?>[\\s\\S]*?</script>";
        //定义style的正则表达式
        String regEx_style = "<style[^>]*?>[\\s\\S]*?</style>";
        //定义HTML标签的正则表达式
        String regEx_html = "<[^>]+>";

        //过滤script标签
        Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll("");

        //过滤style标签
        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll("");

        //过滤html标签
        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll("");

        //返回文本字符串
        return htmlStr.trim();
    }

    /**
     * 格式化字符串<br>
     * 此方法只是简单将占位符 {} 按照顺序替换为参数<br>
     * 如果想输出 {} 使用 \\转义 { 即可，如果想输出 {} 之前的 \ 使用双转义符 \\\\ 即可<br>
     * 例：<br>
     * 通常使用：format("this is {} for {}", "a", "b") =》 this is a for b<br>
     * 转义{}： format("this is \\{} for {}", "a", "b") =》 this is \{} for a<br>
     * 转义\： format("this is \\\\{} for {}", "a", "b") =》 this is \a for b<br>
     *
     * @param strPattern 字符串模板
     * @param argArray   参数列表
     * @return 结果
     */
    public static String format(final String strPattern, final Object... argArray) {
        if (isBlank(strPattern) || argArray == null || argArray.length <= 0) {
            return strPattern;
        }
        final int strPatternLength = strPattern.length();
        // 初始化定义好的长度以获得更好的性能
        StringBuilder sbuf = new StringBuilder(strPatternLength + 50);
        // 记录已经处理到的位置
        int handledPosition = 0;
        // 占位符所在位置
        int delimIndex;
        for (int argIndex = 0; argIndex < argArray.length; argIndex++) {
            delimIndex = strPattern.indexOf(EMPTY_JSON, handledPosition);
            if (delimIndex == -1) {// 剩余部分无占位符
                if (handledPosition == 0) { // 不带占位符的模板直接返回
                    return strPattern;
                }
                // 字符串模板剩余部分不再包含占位符，加入剩余部分后返回结果
                sbuf.append(strPattern, handledPosition, strPatternLength);
                return sbuf.toString();
            }
            // 转义符
            if (delimIndex > 0 && strPattern.charAt(delimIndex - 1) == C_BACKSLASH) {
                if (delimIndex > 1 && strPattern.charAt(delimIndex - 2) == C_BACKSLASH) {// 双转义符
                    // 转义符之前还有一个转义符，占位符依旧有效
                    sbuf.append(strPattern, handledPosition, delimIndex - 1);
                    sbuf.append(argArray[argIndex]);
                    handledPosition = delimIndex + 2;
                } else {
                    // 占位符被转义
                    argIndex--;
                    sbuf.append(strPattern, handledPosition, delimIndex - 1);
                    sbuf.append(C_DELIM_START);
                    handledPosition = delimIndex + 1;
                }
            } else {// 正常占位符
                sbuf.append(strPattern, handledPosition, delimIndex);
                sbuf.append(argArray[argIndex]);
                handledPosition = delimIndex + 2;
            }
        }
        // append the characters following the last {} pair.
        // 加入最后一个占位符后所有的字符
        sbuf.append(strPattern, handledPosition, strPattern.length());
        return sbuf.toString();
    }
}
