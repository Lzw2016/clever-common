package org.clever.common.server.config;

import com.ecwid.consul.v1.ConsulClient;
import lombok.extern.slf4j.Slf4j;
import org.clever.common.server.consul.ConsulRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.consul.discovery.ConsulDiscoveryProperties;
import org.springframework.cloud.consul.discovery.HeartbeatProperties;
import org.springframework.cloud.consul.discovery.TtlScheduler;
import org.springframework.cloud.consul.serviceregistry.ConsulServiceRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 作者： lzw<br/>
 * 创建时间：2017-12-04 10:37 <br/>
 */
@Configuration("CleverCommonServerBeanConfiguration")
@Slf4j
public class DefaultBeanConfiguration {

    @Autowired(required = false)
    private TtlScheduler ttlScheduler;

    @Bean
    @ConditionalOnProperty(name = "spring.cloud.consul.enabled", havingValue = "true", matchIfMissing = true)
    protected ConsulServiceRegistry consulServiceRegistry(
            ConsulClient consulClient,
            ConsulDiscoveryProperties properties,
            HeartbeatProperties heartbeatProperties) {
        return new ConsulRegistry(consulClient, properties, ttlScheduler, heartbeatProperties);
    }
}
