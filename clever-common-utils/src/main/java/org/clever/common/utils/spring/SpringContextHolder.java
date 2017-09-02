package org.clever.common.utils.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;

/**
 * 获取Spring ApplicationContext容器的类<br/>
 * 1.以静态变量保存Spring ApplicationContext<br/>
 * 2.可在任何代码任何地方任何时候取出ApplicaitonContext<br/>
 * 3.提供获取Spring容器中的Bean的方法<br/>
 * <p>
 * 作者：LiZW <br/>
 * 创建时间：2016-5-9 14:25 <br/>
 */
@Slf4j
public class SpringContextHolder {

    /**
     * Spring ApplicationContext容器
     */
    private static ApplicationContext applicationContext = null;

    /**
     * Servlet容器的上下文
     */
    private static ServletContext servletContext = null;

    /**
     * 实现ApplicationContextAware接口, 注入Context到静态变量中.
     */
    public static void setApplicationContext(ApplicationContext applicationContext) {
        if (SpringContextHolder.applicationContext != null) {
            log.info("SpringContextHolder中的ApplicationContext被覆盖, 原有ApplicationContext为:" + SpringContextHolder.applicationContext);
        }
        SpringContextHolder.applicationContext = applicationContext;
    }

    /**
     * 获取Spring容器applicationContext对象
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 获取系统根目录
     */
    public static String getRootRealPath() {
        String rootRealPath = "";
        try {
            rootRealPath = applicationContext.getResource("").getFile().getAbsolutePath();
        } catch (Throwable e) {
            log.warn("获取系统根目录失败", e);
        }
        return rootRealPath;
    }

    /**
     * 获取资源根目录
     */
    public static String getResourceRootRealPath() {
        String rootRealPath = "";
        try {
            rootRealPath = new DefaultResourceLoader().getResource("").getFile().getAbsolutePath();
        } catch (Throwable e) {
            log.warn("获取资源根目录失败", e);
        }
        return rootRealPath;
    }

    /**
     * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
     *
     * @return 返回Bean对象，失败返回null
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        try {
            return (T) applicationContext.getBean(name);
        } catch (Throwable e) {
            log.error("获取Bean失败 name=" + name, e);
            return null;
        }
    }

    /**
     * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
     *
     * @return 返回Bean对象，失败返回null
     */
    public static <T> T getBean(Class<T> requiredType) {
        try {
            return applicationContext.getBean(requiredType);
        } catch (Throwable e) {
            log.error("获取Bean失败 class=" + requiredType, e);
            return null;
        }
    }

    /**
     * @return Servlet容器的上下文
     */
    private static ServletContext getServletContext() {
        if (servletContext == null) {
            WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
            servletContext = webApplicationContext.getServletContext();
        }
        return servletContext;
    }
}
