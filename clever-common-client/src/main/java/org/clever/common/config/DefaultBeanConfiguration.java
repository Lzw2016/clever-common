//package org.clever.common.config;
//
//import feign.Logger;
//import feign.MethodMetadata;
//import lombok.extern.slf4j.Slf4j;
//import org.clever.common.annotation.NotDecodeSlash;
//import org.springframework.cloud.openfeign.support.SpringMvcContract;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.lang.annotation.Annotation;
//import java.lang.reflect.Method;
//
///**
// * 作者： lzw<br/>
// * 创建时间：2018-10-01 16:34 <br/>
// */
//@Configuration("clever-common-client_DefaultBeanConfiguration")
//@Slf4j
//public class DefaultBeanConfiguration {
//
//    @Bean
//    protected Logger.Level feignLoggerLevel() {
//        return Logger.Level.FULL;
//    }
//
//    /**
//     * 解决url path变量url编码问题
//     */
//    // @Bean
//    protected SpringMvcContract decodeSlashContract() {
//        return new SpringMvcContract() {
//            @Override
//            protected void processAnnotationOnMethod(MethodMetadata data, Annotation methodAnnotation, Method method) {
//                if (!(methodAnnotation instanceof NotDecodeSlash) && !methodAnnotation.annotationType().isAnnotationPresent(NotDecodeSlash.class)) {
//                    data.template().decodeSlash(false);
//                }
//                super.processAnnotationOnMethod(data, methodAnnotation, method);
//            }
//        };
//    }
//}
