package org.clever.common.utils.mapper.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.clever.common.utils.DateTimeUtils;

import java.io.IOException;
import java.util.Date;

/**
 * 自定义反序列化时间
 * 作者： lzw<br/>
 * 创建时间：2019-08-18 15:47 <br/>
 */
@Slf4j
public class CustomizerDateDeserializer extends JsonDeserializer<Date> {
    public final static CustomizerDateDeserializer instance = new CustomizerDateDeserializer();

    @Override
    public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        if (StringUtils.isBlank(p.getText())) {
            return null;
        }
        String str = StringUtils.trim(p.getText());
        Date result = DateTimeUtils.parseDate(str);
        log.debug("[CustomizerDateDeserializer]-时间反序列化 [{}]->[{}]", str, result);
        return result;
    }
}
