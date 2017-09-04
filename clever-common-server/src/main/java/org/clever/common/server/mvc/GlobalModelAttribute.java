package org.clever.common.server.mvc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 全局ModelAttribute
 * <p>
 * 作者：lzw <br/>
 * 创建时间：2017-09-03 12:02 <br/>
 */
@Slf4j
@ControllerAdvice
public class GlobalModelAttribute {
    /**
     * 此处将键值对添加到全局，注解了@RequestMapping的方法都可以获得此键值对
     */
    @ModelAttribute
    public void addAttributes(Model model, HttpServletRequest request, HttpServletResponse response) {
        // model.addAttribute("key", "value");
    }
}
