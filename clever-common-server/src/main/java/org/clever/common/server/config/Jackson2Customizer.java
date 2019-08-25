package org.clever.common.server.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.extern.slf4j.Slf4j;
import org.clever.common.utils.mapper.jackson.CustomizerDateDeserializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

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
}
