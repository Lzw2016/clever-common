package org.clever.common.server.consul;

import com.ecwid.consul.v1.ConsulClient;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.consul.discovery.ConsulDiscoveryProperties;
import org.springframework.cloud.consul.discovery.HeartbeatProperties;
import org.springframework.cloud.consul.discovery.TtlScheduler;
import org.springframework.cloud.consul.serviceregistry.ConsulRegistration;
import org.springframework.cloud.consul.serviceregistry.ConsulServiceRegistry;

import java.util.List;
import java.util.stream.Collectors;

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
        // TODO 做到全局唯一 IP + PORT
        reg.getService().setId(reg.getService().getName() + "-" + reg.getService().getAddress() + "-" + reg.getService().getPort());
        // reg.getService().setId(reg.getService().getName() + "-" + SnowFlake.SNOW_FLAKE.nextId());

        // TODO 增加自定义Tags
        // extTagsSetIntoService(reg,dayuProperties.getDebugTag(),"");
        // extTagsSetIntoService(reg,dayuProperties.getContainerTags(), "");
        super.register(reg);
    }

    /**
     * 服务注册时扩展当前服务的Tags
     *
     * @param reg     ConsulRegistration实例
     * @param tagsStr tags字符串
     * @param prefix  tag前缀
     */
    private void extTagsSetIntoService(ConsulRegistration reg, String tagsStr, String prefix) {
        if (StringUtils.isBlank(tagsStr)) {
            return;
        }
        List<String> extTags = Lists.newArrayList(tagsStr.split(",")).stream().filter(StringUtils::isNotBlank).collect(Collectors.toList());
        extTags = extTags.stream().map(tag -> prefix + tag).collect(Collectors.toList());
        List<String> tags = reg.getService().getTags();
        tags.addAll(extTags);
        reg.getService().setTags(tags);
    }
}
