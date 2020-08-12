package org.clever.common.utils.imgvalidate;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/08/12 22:15 <br/>
 */
@Slf4j
public class ImageValidatePatchcaUtilsTest {

    @SneakyThrows
    @Test
    public void t01() {
        for (int i = 0; i < 20; i++) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            String code = ImageValidatePatchcaUtils.createImageStream(out);
            log.info("code -> {}", code);
            FileUtils.writeByteArrayToFile(new File("D:\\" + code + ".png"), out.toByteArray());
            out.close();
        }
    }
}
