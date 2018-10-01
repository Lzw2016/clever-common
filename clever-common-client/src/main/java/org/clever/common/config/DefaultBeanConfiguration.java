package org.clever.common.config;

import feign.Logger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-10-01 16:34 <br/>
 */
@Configuration("CleverCommonClientBeanConfiguration")
@Slf4j
public class DefaultBeanConfiguration {

    @Bean
    protected Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}
