package org.clever.common.utils.test;

import lombok.extern.slf4j.Slf4j;
import org.clever.common.utils.mapper.JacksonMapper;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 作者： lzw<br/>
 * 创建时间：2019-08-24 22:55 <br/>
 */
@Slf4j
public class JacksonMapperTest {

    @Test
    public void t1() {
        Map<String, Object> tmp = new HashMap<>();
        tmp.put("1", 1);
        tmp.put("2", 2.2F);
        tmp.put("3", 3.3D);
        tmp.put("4", 4L);
        tmp.put("5", false);
        tmp.put("6", "nashorn");
        tmp.put("7", new Date());
        tmp.put("8", new Object[]{1, 2.2F, 3.3D, 4L, true, "nashorn", new Date()});
        log.info("{}", JacksonMapper.getInstance().toJson(tmp));
    }
}
