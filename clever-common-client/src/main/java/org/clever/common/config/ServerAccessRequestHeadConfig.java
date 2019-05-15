package org.clever.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 服务之间访问的全局请求头设置
 * <p>
 * 作者： lzw<br/>
 * 创建时间：2018-03-15 14:31 <br/>
 */
@ConfigurationProperties(prefix = "feign.global.request")
@Component
@Data
public class ServerAccessRequestHeadConfig {

    /**
     * 服务之间访问的全局请求头设置
     */
    private Map<String, String> heads;
}
