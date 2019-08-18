package org.clever.common.utils.test;

import lombok.extern.slf4j.Slf4j;
import org.clever.common.utils.StringUtils;
import org.junit.Test;

/**
 * 作者： lzw<br/>
 * 创建时间：2019-08-18 18:23 <br/>
 */
@Slf4j
public class StringUtilsTest {

    @Test
    public void formatTest() {
        //通常使用
        String result1 = StringUtils.format("this is {} for {}", "a", "b");
        log.info(result1);

        //转义{}
        String result2 = StringUtils.format("this is \\{} for {}", "a", "b");
        log.info(result2);

        //转义\
        String result3 = StringUtils.format("this is \\\\{} for {}", "a", "b");
        log.info(result3);
    }
}
