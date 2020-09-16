package org.clever.common.server.mvc;

import com.alibaba.excel.exception.ExcelAnalysisException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.clever.common.exception.BusinessException;
import org.clever.common.model.ValidMessage;
import org.clever.common.model.response.ErrorResponse;
import org.clever.common.utils.mapper.JacksonMapper;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 全局异常处理
 * <p>
 * 作者：lzw <br/>
 * 创建时间：2017-09-03 11:55 <br/>
 */
@SuppressWarnings("Duplicates")
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler implements HandlerExceptionResolver {

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

    private List<ValidMessage> getValidMessages(Set<? extends ConstraintViolation<?>> constraintViolations) {
        List<ValidMessage> validMessageList = new ArrayList<>();
        for (ConstraintViolation<?> violation : constraintViolations) {
            ValidMessage validMessage = new ValidMessage();
            validMessage.setEntityName(violation.getRootBeanClass().getSimpleName());
            validMessage.setFiled(violation.getPropertyPath().toString());
            validMessage.setValue(violation.getInvalidValue() == null ? "null" : violation.getInvalidValue().toString());
            validMessage.setErrorMessage(violation.getMessage());
            validMessage.setCode(violation.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName());
            validMessageList.add(validMessage);
        }
        return validMessageList;
    }

    /**
     * 创建默认的异常信息
     */
    private ErrorResponse newErrorResponse(HttpServletRequest request, HttpServletResponse response, Throwable e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setPath(request.getRequestURI());
        errorResponse.setException(e.getClass().getName());
        errorResponse.setError(e.getMessage());
        errorResponse.setMessage("服务器内部错误");
        errorResponse.setStatus(response.getStatus());
        errorResponse.setTimestamp(new Date());
        return errorResponse;
    }

    /**
     * 数据主键重复
     */
    @ExceptionHandler(value = DuplicateKeyException.class)
    protected ErrorResponse defaultErrorHandler(HttpServletRequest request, HttpServletResponse response, DuplicateKeyException e) {
        log.debug("[ExceptionHandler]-全局的异常处理  ", e);
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        ErrorResponse errorResponse = newErrorResponse(request, response, e);
        errorResponse.setMessage("保存数据失败，数据已经存在");
        return errorResponse;
    }

    /**
     * 解析Excel文件异常
     */
    @ExceptionHandler(value = ExcelAnalysisException.class)
    protected ErrorResponse defaultErrorHandler(HttpServletRequest request, HttpServletResponse response, ExcelAnalysisException e) {
        log.debug("[ExceptionHandler]-全局的异常处理  ", e);
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        ErrorResponse errorResponse = newErrorResponse(request, response, e);
        errorResponse.setMessage(e.getCause().getMessage());
        return errorResponse;
    }

    /**
     * 文件上传大小超过配置的最大值
     */
    @ExceptionHandler(value = MaxUploadSizeExceededException.class)
    protected ErrorResponse defaultErrorHandler(HttpServletRequest request, HttpServletResponse response, MaxUploadSizeExceededException e) {
        log.debug("[ExceptionHandler]-全局的异常处理  ", e);
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        ErrorResponse errorResponse = newErrorResponse(request, response, e);
        errorResponse.setMessage("上传文件大小超限");
        return errorResponse;
    }

    /**
     * 数据校验异常
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    protected ErrorResponse defaultErrorHandler(HttpServletRequest request, HttpServletResponse response, ConstraintViolationException e) {
        log.debug("[ExceptionHandler]-全局的异常处理  ", e);
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        ErrorResponse errorResponse = newErrorResponse(request, response, e);
        errorResponse.setValidMessageList(getValidMessages(e.getConstraintViolations()));
        errorResponse.setMessage("请求参数校验失败");
        return errorResponse;
    }

    /**
     * 数据校验异常
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    protected ErrorResponse defaultErrorHandler(HttpServletRequest request, HttpServletResponse response, MethodArgumentNotValidException e) {
        log.debug("[ExceptionHandler]-全局的异常处理  ", e);
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        ErrorResponse errorResponse = newErrorResponse(request, response, e);
        errorResponse.setValidMessageList(getValidMessages(e.getBindingResult()));
        errorResponse.setMessage("请求参数校验失败");
        return errorResponse;
    }

    /**
     * 数据校验异常
     */
    @ExceptionHandler(value = BindException.class)
    protected ErrorResponse defaultErrorHandler(HttpServletRequest request, HttpServletResponse response, BindException e) {
        log.debug("[ExceptionHandler]-全局的异常处理  ", e);
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        ErrorResponse errorResponse = newErrorResponse(request, response, e);
        errorResponse.setValidMessageList(getValidMessages(e.getBindingResult()));
        errorResponse.setMessage("请求参数校验失败");
        return errorResponse;
    }

    /**
     * 请求参数转换异常
     */
    @ExceptionHandler(value = HttpMessageConversionException.class)
    protected ErrorResponse defaultErrorHandler(HttpServletRequest request, HttpServletResponse response, HttpMessageConversionException e) {
        log.debug("[ExceptionHandler]-全局的异常处理  ", e);
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        ErrorResponse errorResponse = newErrorResponse(request, response, e);
        errorResponse.setMessage("请求参数转换异常");
        return errorResponse;
    }

    /**
     * 请求参数校验异常
     */
    @ExceptionHandler(value = ValidationException.class)
    protected ErrorResponse defaultErrorHandler(HttpServletRequest request, HttpServletResponse response, ValidationException e) {
        log.debug("[ExceptionHandler]-全局的异常处理  ", e);
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        ErrorResponse errorResponse = newErrorResponse(request, response, e);
        errorResponse.setMessage("请求参数校验异常");
        return errorResponse;
    }

    /**
     * 业务异常处理方法<br/>
     */
    @ExceptionHandler(value = BusinessException.class)
    protected ErrorResponse defaultErrorHandler(HttpServletRequest request, HttpServletResponse response, BusinessException e) {
        log.debug("[ExceptionHandler]-全局的异常处理  ", e);
        if (e.getStatus() != null) {
            response.setStatus(e.getStatus());
        } else {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
        }
        ErrorResponse errorResponse = newErrorResponse(request, response, e);
        errorResponse.setError("业务异常");
        errorResponse.setMessage(e.getMessage());
        return errorResponse;
    }

    /**
     * 默认的异常处理方法<br/>
     */
    @ExceptionHandler(value = Throwable.class)
    protected ErrorResponse defaultErrorHandler(HttpServletRequest request, HttpServletResponse response, Throwable e) {
        log.debug("[ExceptionHandler]-全局的异常处理  ", e);
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        ErrorResponse errorResponse = newErrorResponse(request, response, e);
        errorResponse.setMessage("服务器内部错误");
        return errorResponse;
    }

    @SneakyThrows
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        ErrorResponse res;
        if (ex instanceof DuplicateKeyException) {
            res = defaultErrorHandler(request, response, (DuplicateKeyException) ex);
        } else if (ex instanceof ExcelAnalysisException) {
            res = defaultErrorHandler(request, response, (ExcelAnalysisException) ex);
        } else if (ex instanceof MaxUploadSizeExceededException) {
            res = defaultErrorHandler(request, response, (MaxUploadSizeExceededException) ex);
        } else if (ex instanceof ConstraintViolationException) {
            res = defaultErrorHandler(request, response, (ConstraintViolationException) ex);
        } else if (ex instanceof MethodArgumentNotValidException) {
            res = defaultErrorHandler(request, response, (MethodArgumentNotValidException) ex);
        } else if (ex instanceof BindException) {
            res = defaultErrorHandler(request, response, (BindException) ex);
        } else if (ex instanceof HttpMessageConversionException) {
            res = defaultErrorHandler(request, response, (HttpMessageConversionException) ex);
        } else if (ex instanceof ValidationException) {
            res = defaultErrorHandler(request, response, (ValidationException) ex);
        } else if (ex instanceof BusinessException) {
            res = defaultErrorHandler(request, response, (BusinessException) ex);
        } else {
            res = defaultErrorHandler(request, response, ex);
        }
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().print(JacksonMapper.getInstance().toJson(res));
        return null;
    }
}
