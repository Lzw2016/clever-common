package org.clever.common.server.consul;

import com.ecwid.consul.v1.ConsulClient;
import org.springframework.cloud.consul.discovery.ConsulDiscoveryProperties;
import org.springframework.cloud.consul.discovery.HeartbeatProperties;
import org.springframework.cloud.consul.discovery.TtlScheduler;
import org.springframework.cloud.consul.serviceregistry.ConsulRegistration;
import org.springframework.cloud.consul.serviceregistry.ConsulServiceRegistry;

/**
 * 自定义Consul服务注册逻辑
 * <p>
 * 作者： lzw<br/>
 * 创建时间：2018-09-30 9:29 <br/>
 */
public class ConsulRegistry extends ConsulServiceRegistry {

    public ConsulRegistry(
            ConsulClient client,
            ConsulDiscoveryProperties properties,
            TtlScheduler ttlScheduler,
            HeartbeatProperties heartbeatProperties) {
        super(client, properties, ttlScheduler, heartbeatProperties);
    }

    /**
     * 重设服务实例ID
     */
    @Override
    public void register(ConsulRegistration reg) {
        // TODO 做到全局唯一
        reg.getService().setId(reg.getService().getName() + "-" + reg.getService().getAddress() + "-" + reg.getService().getPort());
        // reg.getService().setId(reg.getService().getName() + "-" + SnowFlake.SNOW_FLAKE.nextId());
        super.register(reg);
    }
}
