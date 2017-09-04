package org.clever.common.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.ValidMessage;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
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
     * 响应状态码
     */
    private int status;

    /**
     * 错误消息
     */
    private String error;

    /**
     * 异常类型
     */
    private String exception;

    /**
     * 异常消息(exception.message)
     */
    private String message;

    /**
     * 请求路径
     */
    private String path;

    /**
     * 表单数据验证的错误消息
     */
    private List<ValidMessage> validMessageList;

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
