package org.clever.common.server.mvc;

import lombok.extern.slf4j.Slf4j;
import org.clever.common.model.ValidMessage;
import org.clever.common.model.exception.BusinessException;
import org.clever.common.model.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 全局异常处理
 * <p>
 * 作者：lzw <br/>
 * 创建时间：2017-09-03 11:55 <br/>
 */
@SuppressWarnings("Duplicates")
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 获取请求参数校验错误信息
     *
     * @return 请求参数校验没有错误返回null
     */
    private List<ValidMessage> getValidMessages(BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            return null;
        }
        List<ValidMessage> validMessageList = new ArrayList<>();
        List<FieldError> validError = bindingResult.getFieldErrors();
        for (FieldError fieldError : validError) {
            validMessageList.add(new ValidMessage(fieldError));
        }
        return validMessageList;
    }

    /**
     * 数据校验异常
     */
    @ResponseBody
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    protected ErrorResponse defaultErrorHandler(HttpServletRequest request, HttpServletResponse response, MethodArgumentNotValidException e) {
        log.debug("[ExceptionHandler]-全局的异常处理  ", e);
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setValidMessageList(getValidMessages(e.getBindingResult()));
        errorResponse.setPath(request.getRequestURI());
        errorResponse.setException(e.getClass().getName());
        errorResponse.setMessage(e.getMessage());
        errorResponse.setError("请求参数错误");
        errorResponse.setStatus(response.getStatus());
        errorResponse.setTimestamp(new Date());
        return errorResponse;
    }

    /**
     * 数据校验异常
     */
    @ResponseBody
    @ExceptionHandler(value = BindException.class)
    protected ErrorResponse defaultErrorHandler(HttpServletRequest request, HttpServletResponse response, BindException e) {
        log.debug("[ExceptionHandler]-全局的异常处理  ", e);
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setValidMessageList(getValidMessages(e.getBindingResult()));
        errorResponse.setPath(request.getRequestURI());
        errorResponse.setException(e.getClass().getName());
        errorResponse.setMessage(e.getMessage());
        errorResponse.setError("请求参数错误");
        errorResponse.setStatus(response.getStatus());
        errorResponse.setTimestamp(new Date());
        return errorResponse;
    }

    /**
     * 业务异常处理方法<br/>
     */
    @ResponseBody
    @ExceptionHandler(value = BusinessException.class)
    protected ErrorResponse defaultErrorHandler(HttpServletRequest request, HttpServletResponse response, BusinessException e) {
        log.debug("[ExceptionHandler]-全局的异常处理  ", e);
        if (e.getStatus() != null) {
            response.setStatus(e.getStatus());
        } else {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
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
