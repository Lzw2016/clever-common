package org.clever.common.server.mvc;

import lombok.extern.slf4j.Slf4j;
import org.clever.common.model.exception.BusinessException;
import org.clever.common.model.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 全局异常处理
 * <p>
 * 作者：lzw <br/>
 * 创建时间：2017-09-03 11:55 <br/>
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 默认的异常处理方法<br/>
     */
    @ResponseBody
    @ExceptionHandler(value = BusinessException.class)
    protected ErrorResponse defaultErrorHandler(HttpServletRequest request, HttpServletResponse response, BusinessException e) {
        log.debug("[ExceptionHandler]-全局的异常处理  ", e);
        if (e.getStatus() != null) {
            response.setStatus(e.getStatus());
        }
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setPath(request.getRequestURI());
        errorResponse.setException(e.getClass().getName());
        errorResponse.setMessage(e.getMessage());
        errorResponse.setError("业务异常");
        errorResponse.setStatus(response.getStatus());
        errorResponse.setTimestamp(new Date());
        return errorResponse;
    }

    /**
     * 默认的异常处理方法<br/>
     */
    @ResponseBody
    @ExceptionHandler(value = Throwable.class)
    protected ErrorResponse defaultErrorHandler(HttpServletRequest request, HttpServletResponse response, Throwable e) {
        log.debug("[ExceptionHandler]-全局的异常处理  ", e);
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setPath(request.getRequestURI());
        errorResponse.setException(e.getClass().getName());
        errorResponse.setMessage(e.getMessage());
        errorResponse.setError("服务器内部错误");
        errorResponse.setStatus(response.getStatus());
        errorResponse.setTimestamp(new Date());
        return errorResponse;
    }
}
