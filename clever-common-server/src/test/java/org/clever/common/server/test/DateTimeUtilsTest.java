package org.clever.common.server.test;

import lombok.extern.slf4j.Slf4j;
import org.clever.common.utils.DateTimeUtils;
import org.junit.Test;

import java.util.Date;

/**
 * 作者： lzw<br/>
 * 创建时间：2019-08-18 13:31 <br/>
 */
@Slf4j
public class DateTimeUtilsTest {

    @Test
    public void t() {
        Date date;
        date = DateTimeUtils.parseDate("2019-08-18 05:34:11");
        log.info("### {}", DateTimeUtils.formatToString(date));
        // log.info("### {}", date);

        date = DateTimeUtils.parseDate("2019-08-18T05:34:11.680Z");
        log.info("### {}", DateTimeUtils.formatToString(date));

        date = DateTimeUtils.parseDate("2019-08-18T05:34:11.680+0800");
        log.info("### {}", DateTimeUtils.formatToString(date));

        date = DateTimeUtils.parseDate("20190818 05:34:11");
        log.info("### {}", DateTimeUtils.formatToString(date));
        // log.info("### {}", date.getTime());

        date = DateTimeUtils.parseDate(date.getTime());
        log.info("### {}", DateTimeUtils.formatToString(date));

        date = DateTimeUtils.parseDate(String.valueOf(date.getTime()));
        log.info("### {}", DateTimeUtils.formatToString(date));

        date = DateTimeUtils.parseDate("20190818");
        log.info("### {}", DateTimeUtils.formatToString(date));
    }
}
