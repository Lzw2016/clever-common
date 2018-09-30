package org.clever.common.server.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-09-30 14:17 <br/>
 */
@Configuration
@EnableSwagger2
public class DefaultSwagger2Config {

    @Bean
    public Docket createBootApi() {
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("SpringBoot")
                // .description("description")
                // .termsOfServiceUrl("termsOfServiceUrl")
                .version("")
                // .license("license")
                // .licenseUrl("licenseUrl")
                // .termsOfServiceUrl("termsOfServiceUrl")
                // .contact(contact)
                // .extensions()
                .build();
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo)
                .groupName("SpringBoot")
                .select()
                .apis(RequestHandlerSelectors.basePackage("org.springframework.boot"))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    public Docket createCloudApi() {
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("SpringCloud")
                // .description("description")
                // .termsOfServiceUrl("termsOfServiceUrl")
                .version("")
                // .license("license")
                // .licenseUrl("licenseUrl")
                // .termsOfServiceUrl("termsOfServiceUrl")
                // .contact(contact)
                // .extensions()
                .build();
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo)
                .groupName("SpringCloud")
                .select()
                .apis(RequestHandlerSelectors.basePackage("org.springframework.cloud"))
                .paths(PathSelectors.any())
                .build();
    }
}
