package org.clever.common.exception;

/**
 * 自定义数据验证异常
 * 作者： lzw<br/>
 * 创建时间：2019-08-18 16:46 <br/>
 */
public class ValidateException extends RuntimeException {

    /**
     * @param message 异常信息
     */
    public ValidateException(String message) {
        super(message);
    }

    /**
     * @param message 异常信息
     * @param cause   异常
     */
    public ValidateException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param cause 异常信息
     */
    public ValidateException(Throwable cause) {
        super(cause);
    }
}
