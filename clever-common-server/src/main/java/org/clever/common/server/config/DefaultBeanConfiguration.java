package org.clever.common.server.config;

import com.ecwid.consul.v1.ConsulClient;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.clever.common.server.consul.ConsulRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.consul.discovery.ConsulDiscoveryProperties;
import org.springframework.cloud.consul.discovery.HeartbeatProperties;
import org.springframework.cloud.consul.discovery.TtlScheduler;
import org.springframework.cloud.consul.serviceregistry.ConsulServiceRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

/**
 * 作者： lzw<br/>
 * 创建时间：2017-12-04 10:37 <br/>
 */
@Configuration("CleverCommonServerBeanConfiguration")
@Slf4j
public class DefaultBeanConfiguration {
    /**
     * 自定义Consul注册逻辑
     */
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Bean
    @ConditionalOnProperty(name = "spring.cloud.consul.enabled", havingValue = "true", matchIfMissing = true)
    protected ConsulServiceRegistry consulServiceRegistry(
            ConsulClient consulClient,
            ConsulDiscoveryProperties properties,
            @Autowired(required = false) TtlScheduler ttlScheduler,
            HeartbeatProperties heartbeatProperties) {
        return new ConsulRegistry(consulClient, properties, ttlScheduler, heartbeatProperties);
    }

    /**
     * 在metrics增加application纬度
     */
    @Bean
    @ConditionalOnProperty(name = "spring.application.name")
    protected MeterRegistryCustomizer<MeterRegistry> metricsCommonTags(Environment environment) {
        String name = environment.getProperty("spring.application.name");
        final String application = StringUtils.hasText(name) ? name : "application";
        return registry -> registry.config().commonTags("application", application);
    }
}
