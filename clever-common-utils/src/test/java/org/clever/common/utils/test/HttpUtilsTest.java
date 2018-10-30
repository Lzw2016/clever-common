package org.clever.common.utils.test;

import lombok.extern.slf4j.Slf4j;
import org.clever.common.utils.HttpUtils;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-10-25 11:46 <br/>
 */
@Slf4j
public class HttpUtilsTest {

    @Test
    public void t01() {
        log.info("### {}", HttpUtils.getInner().getStr("http://10.7.1.74:8765/alarm/job"));
        Map<String, String> map = new HashMap<>();
        map.put("jobName", "测试告警01");
        log.info("### {}", HttpUtils.getInner().getStr("http://10.7.1.74:8765/alarm/job", map));
    }

    @Test
    public void t02() {
        String json = "" +
                "{\n" +
                "    \"body\": \"{ \\\"from\\\": \\\"0\\\", \\\"size\\\": \\\"1\\\" } \",\n" +
                "    \"method\": \"GET\",\n" +
                "    \"path\": \"/per-summary-*/_search\"\n" +
                "}";
        log.info("### {}", HttpUtils.getInner().postStr("http://10.7.1.74:8765/alarm/test/es_query", json));
    }
}
