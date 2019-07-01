package org.clever.common.utils;

import lombok.extern.slf4j.Slf4j;

/**
 * 全局任务处理，JVM内同一时刻只有一个线程执行，直接跳过多余调用
 * <p>
 * 作者： lzw<br/>
 * 创建时间：2018-11-10 19:03 <br/>
 */

@Slf4j
public abstract class GlobalJob {

    private static volatile boolean LOCK = false;

    /**
     * 全局执行的任务(JVM内同一时刻只有一个线程执行)
     */
    protected abstract void internalExecute() throws Throwable;

    /**
     * 任务执行的异常处理
     */
    protected abstract void exceptionHandle(Throwable e);

    /**
     * 任务处理内部逻辑
     *
     * @return 执行成功返回true
     */
    public boolean execute() {
        boolean success = false;
        if (LOCK) {
            return false;
        }
        try {
            LOCK = true;
            internalExecute();
            success = true;
        } catch (Throwable e) {
            exceptionHandle(e);
        } finally {
            LOCK = false;
        }
        return success;
    }
}
