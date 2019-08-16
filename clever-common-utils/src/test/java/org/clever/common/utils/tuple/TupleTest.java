package org.clever.common.utils.tuple;

import lombok.extern.slf4j.Slf4j;
import org.clever.common.utils.tuples.TupleTow;
import org.junit.Test;

/**
 * 作者：lizw <br/>
 * 创建时间：2019/08/16 12:47 <br/>
 */
@Slf4j
public class TupleTest {

    @Test
    public void t2() {
        TupleTow<String, Boolean> tow = TupleTow.creat("123", true);
        for (Object o : tow) {
            log.info("--- {}", o);
        }
        log.info("# 1 -> {}", tow.getValue1());
        log.info("# 2 -> {}", tow.getValue2());
        log.info("# toStr -> {}", tow);
    }
}
