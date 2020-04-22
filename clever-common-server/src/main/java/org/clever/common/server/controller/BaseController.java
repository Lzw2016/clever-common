package org.clever.common.server.controller;

import lombok.extern.slf4j.Slf4j;

/**
 * Controller 基础类
 * <p>
 * 作者：lzw <br/>
 * 创建时间：2017-09-03 11:38 <br/>
 */
@Slf4j
public abstract class BaseController {
    /**
     * 视图页面的后缀
     */
    protected final static String HTML_SUFFIX = ".html";

    /**
     * Http Restful接口的后缀
     */
    protected final static String JSON_SUFFIX = ".json";
}
