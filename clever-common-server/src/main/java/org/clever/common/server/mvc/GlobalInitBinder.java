package org.clever.common.server.mvc;

import lombok.extern.slf4j.Slf4j;
import org.clever.common.utils.DateTimeUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.PropertyEditorSupport;
import java.util.Date;

/**
 * 全局数据绑定
 * <p>
 * 作者：lzw <br/>
 * 创建时间：2017-09-03 12:02 <br/>
 */
@Slf4j
@ControllerAdvice
public class GlobalInitBinder {

    /**
     * 初始化数据绑定,每个请求都会进来这个方法,每次WebDataBinder binder都会是一个新对象<br/>
     * 1.将所有传递进来的String进行HTML编码，防止XSS攻击 <br/>
     * 2.将字段中Date类型转换为String类型<br/>
     */
    @InitBinder
    protected void initBinder(WebDataBinder binder, HttpServletRequest request, HttpServletResponse response) {
//        // 防止XSS攻击
//        if (false) {
//            // String类型转换，将所有传递进来的String进行HTML编码，防止XSS攻击
//            binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
//                @Override
//                public void setAsText(String text) {
//                    // 对请求字符进行HTML编码
//                    setValue(text == null ? null : StringEscapeUtils.escapeHtml4(text.trim()));
//                }
//
//                @Override
//                public String getAsText() {
//                    Object value = getValue();
//                    return value != null ? value.toString() : "";
//                }
//            });
//        }

        // Date 类型转换
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(DateTimeUtils.parseDate(text));
                log.debug("[InitBinder]-全局数据绑定  [{}]->[{}]", text, getAsText());
            }
        });
    }
}
