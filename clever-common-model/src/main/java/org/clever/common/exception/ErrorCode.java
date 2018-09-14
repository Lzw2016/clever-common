package org.clever.common.exception;

import lombok.Getter;

/**
 * 系统错误码
 * <p>
 * 作者：lzw <br/>
 * 创建时间：2017-09-03 11:43 <br/>
 */
@Getter
public enum ErrorCode {

    ;
    /**
     * 错误码
     */
    private int code;

    /**
     * 错误消息
     */
    private String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static String getName(int code) {
        for (ErrorCode errorCode : ErrorCode.values()) {
            if (errorCode.code == code) {
                return errorCode.message;
            }
        }
        return null;
    }
}
