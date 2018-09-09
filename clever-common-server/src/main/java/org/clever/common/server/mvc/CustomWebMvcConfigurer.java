package org.clever.common.server.mvc;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

/**
 * Spring MVC 配置
 * <p>
 * 作者：lzw <br/>
 * 创建时间：2017-09-03 11:48 <br/>
 */
@Configuration
public class CustomWebMvcConfigurer implements WebMvcConfigurer {
    /**
     * 自定义静态资源访问映射
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    }

    /**
     * 确定MIME类型<br/>
     * 1、检查扩展名（如action.json）；<br/>
     * 2、检查Parameter（如action?format=xml）；<br/>
     * 3、检查Accept Header<br/>
     */
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
    }

    /**
     * 配置视图解析器
     */
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
    }
}
