package org.clever.common.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.clever.common.utils.spring.SpringContextHolder;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * 作者：lizw <br/>
 * 创建时间：2017/9/15 21:07 <br/>
 */
@SuppressWarnings("SpringFacetCodeInspection")
@Configuration
@Slf4j
public class ServerAccessRequestHeadRequestInterceptor implements RequestInterceptor {

    /**
     * 服务访问Token
     */
    private ServerAccessRequestHeadConfig requestHeadConfig;

    @Override
    public void apply(RequestTemplate template) {
        if (requestHeadConfig == null) {
            requestHeadConfig = SpringContextHolder.getBean(ServerAccessRequestHeadConfig.class);
            log.info("读取服务之间访问的全局请求头设置: {}", requestHeadConfig.getHeads());
        }
        if (requestHeadConfig.getHeads() != null && requestHeadConfig.getHeads().size() > 0) {
            log.debug("[访问服务的全局请求头] --> {}", requestHeadConfig.getHeads());
            for (Map.Entry<String, String> entry : requestHeadConfig.getHeads().entrySet()) {
                template.header(entry.getKey(), entry.getValue());
            }
        }
    }
}
