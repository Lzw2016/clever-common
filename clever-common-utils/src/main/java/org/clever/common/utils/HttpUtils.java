package org.clever.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * Http请求工具,使用 okhttp 实现
 * <p>
 * 作者：lzw <br/>
 * 创建时间：2017-09-02 20:14 <br/>
 */
@Slf4j
public class HttpUtils {
    /**
     * 返回数据默认编码
     */
    private final static String responseDefaultCharset = "UTF-8";

    // --------------------------------------------------------------------------------------------
    // GET请求
    // --------------------------------------------------------------------------------------------

    /**
     * 生成请求参数(会自动编码)
     *
     * @param paramMap 请求参数集合
     * @return 生成请求参数, 如: keyword=%E7%94&enc=utf-8&wq=%AD%E7%85%B2&pv=j0rxs
     */
    private static String getParamStr(Map<String, String> paramMap) {
        return null;
    }

    /**
     * 使用HTTP GET请求获取数据，支持参数，返回字符串
     *
     * @param url            请求URL地址
     * @param paramMap       请求参数(会自动编码)
     * @param defaultCharset 设置返回结果的编码
     * @return 成功返回请求结果，失败返回null
     */
    public static String httpGetResultStr(final String url, Map<String, String> paramMap, final String defaultCharset) {
        return null;
    }

    /**
     * 使用HTTP GET请求获取数据，支持参数，返回字符串
     *
     * @param url      请求URL地址
     * @param paramMap 请求参数(会自动编码)
     * @return 成功返回请求结果，失败返回null
     */
    public static String httpGetResultStr(final String url, Map<String, String> paramMap) {
        return httpGetResultStr(url, paramMap, null);
    }

    /**
     * 使用HTTP GET请求获取数据，支持参数，返回字符串
     *
     * @param url 请求URL地址
     * @return 成功返回请求结果，失败返回null
     */
    public static String httpGetResultStr(final String url) {
        return httpGetResultStr(url, null, null);
    }

    /**
     * 使用HTTP GET请求获取数据，支持参数，字节数组
     *
     * @param url      请求URL地址
     * @param paramMap 请求参数(会自动编码)
     * @return 成功返回请求结果，失败返回null
     */
    public static byte[] httpGetResultByte(final String url, Map<String, String> paramMap) {
        return null;
    }

    /**
     * 使用HTTP GET请求获取数据，支持参数，字节数组
     *
     * @param url 请求URL地址
     * @return 成功返回请求结果，失败返回null
     */
    public static byte[] httpGetResultByte(final String url) {
        return httpGetResultByte(url, null);
    }

    // --------------------------------------------------------------------------------------------
    // POST请求
    // --------------------------------------------------------------------------------------------

    /**
     * 使用HTTP POST请求获取数据，支持参数，返回字符串
     *
     * @param url            请求URL地址
     * @param paramMap       请求参数(会自动编码)
     * @param defaultCharset 设置返回结果的编码
     * @return 成功返回请求结果，失败返回null
     */
    public static String httpPostGetResultStr(String url, Map<String, String> paramMap, final String defaultCharset) {
        return null;
    }

    /**
     * 使用HTTP POST请求获取数据，支持参数，返回字符串
     *
     * @param url      请求URL地址
     * @param paramMap 请求参数(会自动编码)
     * @return 成功返回请求结果，失败返回null
     */
    public static String httpPostGetResultStr(String url, Map<String, String> paramMap) {
        return httpPostGetResultStr(url, paramMap, null);
    }

    /**
     * 使用HTTP POST请求获取数据，支持参数，返回字符串
     *
     * @param url 请求URL地址
     * @return 成功返回请求结果，失败返回null
     */
    public static String httpPostGetResultStr(String url) {
        return httpPostGetResultStr(url, null, null);
    }

    /**
     * 使用HTTP POST请求获取数据，支持参数，返回字节数组
     *
     * @param url      请求URL地址
     * @param paramMap 请求参数(会自动编码)
     * @return 成功返回请求结果，失败返回null
     */
    public static byte[] httpPostGetResultByte(String url, Map<String, String> paramMap) {
        return null;
    }

    /**
     * 使用HTTP POST请求获取数据，支持参数，返回字节数组
     *
     * @param url 请求URL地址
     * @return 成功返回请求结果，失败返回null
     */
    public static byte[] httpPostGetResultByte(String url) {
        return httpPostGetResultByte(url, null);
    }
}
