package org.clever.common.server.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.clever.common.utils.DateTimeUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Date;

/**
 * 自定义jackson序列化反序列化配置
 * 作者： lzw<br/>
 * 创建时间：2019-08-18 15:09 <br/>
 */
@Configuration("Jackson2Customizer")
@ConditionalOnClass({Jackson2ObjectMapperBuilder.class, ObjectMapper.class})
@Slf4j
public class Jackson2Customizer implements Jackson2ObjectMapperBuilderCustomizer {

    @Override
    public void customize(Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder) {
        // Long -> 序列化
        jacksonObjectMapperBuilder.serializerByType(BigInteger.class, ToStringSerializer.instance);
        jacksonObjectMapperBuilder.serializerByType(Long.class, ToStringSerializer.instance);
        jacksonObjectMapperBuilder.serializerByType(Long.TYPE, ToStringSerializer.instance);
        log.debug("### [jackson] Long -> 序列化 -> ToStringSerializer");
        // Date -> 反序列化
        jacksonObjectMapperBuilder.deserializerByType(Date.class, CustomizerDateDeserializer.instance);
        log.debug("### [jackson] Date -> 反序列化 -> CustomizerDateDeserializer");
    }

    /**
     * 自定义反序列化时间
     * 作者： lzw<br/>
     * 创建时间：2019-08-18 15:47 <br/>
     */
    @Slf4j
    public static class CustomizerDateDeserializer extends JsonDeserializer<Date> {
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
}
