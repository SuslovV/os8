package ru.os8.aop.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfiguration {

    @Bean
//	public OpenAPI customOpenAPI(@Value("${application-description}") String appDesciption,
//								 @Value("${application-version}") String appVersion) {
    public OpenAPI customOpenAPI() {
        String appDesciption = "AOP";
        String appVersion = "";

        return new OpenAPI()
                .info(new Info()
                        .title("AOP API")
                        .version(appVersion)
                        .description(appDesciption)
                        .termsOfService("http://swagger.io/terms/")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }

}
