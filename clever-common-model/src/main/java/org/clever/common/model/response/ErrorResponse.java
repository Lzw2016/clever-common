package org.clever.common.model.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 作者：lzw <br/>
 * 创建时间：2017-09-03 13:31 <br/>
 */
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
}
