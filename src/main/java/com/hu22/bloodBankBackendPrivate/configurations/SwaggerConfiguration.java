package com.hu22.bloodBankBackendPrivate.configurations;

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
    @Bean
    public Docket docket(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(TestApiInfo())
                .select()
                .apis(
                        Predicates.not(
                                RequestHandlerSelectors.basePackage("org.springframework.boot")
                        )
                )
                .build();
    }

    private ApiInfo TestApiInfo(){
        return new ApiInfoBuilder()
                .title("Blood Bank Management")
                .description("All api ")
                .version("v1")
                .build();
    }
}