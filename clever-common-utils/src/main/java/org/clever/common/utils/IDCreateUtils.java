package org.clever.common.utils;

import java.util.UUID;

/**
 * 封装各种生成唯一性ID算法的工具类<br/>
 * <p/>
 * 作者：LiZW <br/>
 * 创建时间：2016-5-8 16:14 <br/>
 */
public class IDCreateUtils {
    /**
     * 封装JDK自带的UUID, 通过Random数字生成, 中间无"-"分割.<br/>
     * 例如：57d7058dbc79444db7e57a5d0b955cc8<br/>
     */
    public static String uuidNotSplit() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 封装JDK自带的UUID<br/>
     * 例如：57d7058d-bc79-444d-b7e5-7a5d0b955cc8<br/>
     */
    public static String uuid() {
        return UUID.randomUUID().toString();
    }

    /**
     * 以62进制（字母加数字）生成19位UUID，最短的UUID
     */
    public static String shortUuid() {
        UUID uuid = UUID.randomUUID();
        return digits(uuid.getMostSignificantBits() >> 32, 8) +
                digits(uuid.getMostSignificantBits() >> 16, 4) +
                digits(uuid.getMostSignificantBits(), 4) +
                digits(uuid.getLeastSignificantBits() >> 48, 4) +
                digits(uuid.getLeastSignificantBits(), 12);
    }

    private static String digits(long val, int digits) {
        long hi = 1L << (digits * 4);
        return CustomNumbers.toString(hi | (val & (hi - 1)), CustomNumbers.MAX_RADIX)
                .substring(1);
    }
}
