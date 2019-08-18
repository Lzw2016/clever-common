package org.clever.common.utils.test;

import lombok.extern.slf4j.Slf4j;
import org.clever.common.utils.NetUtil;
import org.junit.Test;

/**
 * 作者： lzw<br/>
 * 创建时间：2019-08-18 18:42 <br/>
 */
@Slf4j
public class NetUtilTest {

    @Test
    public void t01() {
        log.info("{}", NetUtil.getUsableLocalPort());
        log.info("{}", NetUtil.getUsableLocalPorts(50, NetUtil.PORT_RANGE_MIN, NetUtil.PORT_RANGE_MAX));
    }
}
