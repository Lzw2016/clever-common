package org.clever.common.model.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统业务异常
 * <p>
 * 作者：lzw <br/>
 * 创建时间：2017-09-03 11:45 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * 请求响应状态吗
     */
    private Integer status;
}
