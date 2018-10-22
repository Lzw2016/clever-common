package org.clever.common.utils.test;

import lombok.extern.slf4j.Slf4j;
import org.clever.common.utils.IDCreateUtils;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-10-13 11:38 <br/>
 */
@Slf4j
public class IDCreateUtilsTest {

    @Test
    public void t01() {
        int max = 0;
        int count = 10000 * 1000 * 2;
        Set<String> uuidSet = new HashSet<>(count);
        long start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            String uuid = IDCreateUtils.shortUuid();
            uuidSet.add(uuid);
            int tmp = uuid.length();
            if (max < tmp) {
                max = tmp;
            }
        }
        log.info("### 时间{}， 最大长度{}， 重复数量{}", (System.currentTimeMillis() - start) / 1000.0, max, count - uuidSet.size());
    }
}
