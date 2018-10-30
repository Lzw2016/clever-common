package org.clever.common.utils.test;

import lombok.extern.slf4j.Slf4j;
import org.clever.common.utils.javamail.ApacheSendMailUtils;
import org.clever.common.utils.javamail.SpringSendMailUtils;
import org.junit.Test;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-10-30 20:24 <br/>
 */
@Slf4j
public class ApacheSendMailUtilsTest {

    private static final String accountPassword = "";

    @Test
    public void t01() {
        ApacheSendMailUtils apacheSendMailUtils = new ApacheSendMailUtils("love520lzw1000000@163.com", accountPassword);
        log.info("####");
        long start = System.currentTimeMillis();
        apacheSendMailUtils.sendHtmlEmail("lzw1000000@163.com", "测试邮件", "<h3>啦啦啦</h3>");
        log.info("### {}", System.currentTimeMillis() - start);
    }

    @Test
    public void t02() {
        SpringSendMailUtils springSendMailUtils = new SpringSendMailUtils("love520lzw1000000@163.com", accountPassword);
        springSendMailUtils.sendMimeMessage("lzw1000000@163.com", "测试邮件01", "<h3>啦啦啦</h3>");
    }
}
