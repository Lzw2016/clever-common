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
     * 视图页面(JSP)的后缀
     */
    protected final static String VIEW_PAGE_SUFFIX = ".html";

    /**
     * 视图页面(Json)的后缀
     */
    protected final static String JSON_SUFFIX = ".json";
}
