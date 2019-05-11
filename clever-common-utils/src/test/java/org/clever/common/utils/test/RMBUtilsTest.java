package org.clever.common.utils.test;

import org.clever.common.utils.RMBUtils;
import org.junit.Assert;
import org.junit.Test;

public class RMBUtilsTest {

    @Test
    public void testConvert() {

        // 整数
        Assert.assertEquals("零元整", RMBUtils.digitUppercase(0));
        Assert.assertEquals("壹佰贰拾叁元整", RMBUtils.digitUppercase(123));
        Assert.assertEquals("壹佰万元整", RMBUtils.digitUppercase(1000000));
        Assert.assertEquals("壹亿零壹元整", RMBUtils.digitUppercase(100000001));
        Assert.assertEquals("壹拾亿元整", RMBUtils.digitUppercase(1000000000));
        Assert.assertEquals("壹拾贰亿叁仟肆佰伍拾陆万柒仟捌佰玖拾元整", RMBUtils.digitUppercase(1234567890));
        Assert.assertEquals("壹拾亿零壹佰壹拾万零壹佰零壹元整", RMBUtils.digitUppercase(1001100101));
        Assert.assertEquals("壹亿壹仟零壹拾万壹仟零壹拾元整", RMBUtils.digitUppercase(110101010));

        // 小数
        Assert.assertEquals("壹角贰分", RMBUtils.digitUppercase(0.12));
        Assert.assertEquals("壹佰贰拾叁元叁角肆分", RMBUtils.digitUppercase(123.34));
        Assert.assertEquals("壹佰万元伍角陆分", RMBUtils.digitUppercase(1000000.56));
        Assert.assertEquals("壹亿零壹元柒角捌分", RMBUtils.digitUppercase(100000001.78));
        Assert.assertEquals("壹拾亿元玖角", RMBUtils.digitUppercase(1000000000.90));
        Assert.assertEquals("壹拾贰亿叁仟肆佰伍拾陆万柒仟捌佰玖拾元叁分", RMBUtils.digitUppercase(1234567890.03));
        Assert.assertEquals("壹拾亿零壹佰壹拾万零壹佰零壹元整", RMBUtils.digitUppercase(1001100101.00));
        Assert.assertEquals("壹亿壹仟零壹拾万壹仟零壹拾元壹角", RMBUtils.digitUppercase(110101010.10));

        // 负数
        Assert.assertEquals("负壹角贰分", RMBUtils.digitUppercase(-0.12));
        Assert.assertEquals("负壹佰贰拾叁元叁角肆分", RMBUtils.digitUppercase(-123.34));
        Assert.assertEquals("负壹佰万元伍角陆分", RMBUtils.digitUppercase(-1000000.56));
        Assert.assertEquals("负壹亿零壹元柒角捌分", RMBUtils.digitUppercase(-100000001.78));
        Assert.assertEquals("负壹拾亿元玖角", RMBUtils.digitUppercase(-1000000000.90));
        Assert.assertEquals("负壹拾贰亿叁仟肆佰伍拾陆万柒仟捌佰玖拾元叁分", RMBUtils.digitUppercase(-1234567890.03));
        Assert.assertEquals("负壹拾亿零壹佰壹拾万零壹佰零壹元整", RMBUtils.digitUppercase(-1001100101.00));
        Assert.assertEquals("负壹亿壹仟零壹拾万壹仟零壹拾元壹角", RMBUtils.digitUppercase(-110101010.10));
    }
}
