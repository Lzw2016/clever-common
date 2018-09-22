package org.clever.common.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.exception.BusinessException;
import org.clever.common.model.ValidMessage;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 系统异常时返回的数据
 * <p>
 * 作者：lzw <br/>
 * 创建时间：2017-09-03 13:31 <br/>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = true)
@Data
public class ErrorResponse extends BaseResponse {
    private static final long serialVersionUID = 1L;

    /**
     * 时间戳
     */
    private Date timestamp;

    /**
     * 错误消息，用于前端显示
     */
    private String error;

    /**
     * 响应状态码(HTTP 状态码)
     */
    private int status;

    /**
     * 异常类型，异常的具体类型
     */
    private String exception;

    /**
     * 异常消息(exception.message)
     */
    private String message;

    /**
     * 请求路径，当前请求的路径
     */
    private String path;

    /**
     * 表单数据验证的错误消息
     */
    private List<ValidMessage> validMessageList;

    public ErrorResponse() {
        this.timestamp = new Date();
    }

    /**
     * @param error 错误消息，用于前端显示
     */
    public ErrorResponse(String error) {
        this(error, null, 400, null);
    }

    /**
     * @param error  错误消息，用于前端显示
     * @param status 响应状态码
     */
    public ErrorResponse(String error, int status) {
        this(error, null, status, null);
    }

    /**
     * @param error     错误消息，用于前端显示
     * @param exception 异常
     */
    public ErrorResponse(String error, Throwable exception) {
        this(error, exception, (exception instanceof BusinessException) ? 400 : 500, null);
    }

    /**
     * @param error     错误消息，用于前端显示
     * @param exception 异常
     * @param path      请求路径
     */
    public ErrorResponse(String error, Throwable exception, String path) {
        this(error, exception, (exception instanceof BusinessException) ? 400 : 500, path);
    }

    /**
     * @param error     错误消息，用于前端显示
     * @param exception 异常
     * @param status    响应状态码(HTTP 状态码)
     */
    public ErrorResponse(String error, Throwable exception, int status) {
        this(error, exception, status, null);
    }

    /**
     * @param error     错误消息，用于前端显示
     * @param status    响应状态码(HTTP 状态码)
     * @param exception 异常
     * @param path      请求路径
     */
    public ErrorResponse(String error, Throwable exception, int status, String path) {
        this.timestamp = new Date();
        this.error = error;
        this.status = status;
        if (exception != null) {
            this.exception = exception.getClass().getName();
            this.message = exception.getMessage();
        } else {
            this.message = error;
        }
        this.path = path;
    }

    /**
     * 添加请求参数校验错误
     */
    public ErrorResponse addValidMessage(FieldError fieldError) {
        if (validMessageList == null) {
            validMessageList = new ArrayList<>();
        }
        validMessageList.add(new ValidMessage(fieldError));
        return this;
    }

    /**
     * 添加请求参数校验错误
     */
    public ErrorResponse addValidMessage(ValidMessage validMessage) {
        if (validMessageList == null) {
            validMessageList = new ArrayList<>();
        }
        validMessageList.add(validMessage);
        return this;
    }
}
