package org.clever.common.utils;

import lombok.extern.slf4j.Slf4j;

/**
 * 线程工具类
 * 作者： lzw<br/>
 * 创建时间：2019-01-24 16:15 <br/>
 */
@Slf4j
public class ThreadUtils {

    /**
     * 打印线程栈信息
     */
    public static void printTrack(Thread thread) {
        if (thread == null) {
            return;
        }
        StackTraceElement[] stackTrace = thread.getStackTrace();
        StringBuilder stringBuilder = new StringBuilder();
        for (StackTraceElement stackTraceElement : stackTrace) {
            stringBuilder.append(
                    String.format(
                            "%s\tat %s.%s(%s:%s)",
                            System.getProperty("line.separator"),
                            stackTraceElement.getClassName(),
                            stackTraceElement.getMethodName(),
                            stackTraceElement.getFileName(),
                            stackTraceElement.getLineNumber()
                    )
            );
        }
        log.info(stringBuilder.toString());
    }

    /**
     * 打印当前线程栈信息
     */
    public static void printTrack() {
        printTrack(Thread.currentThread());
    }
}
