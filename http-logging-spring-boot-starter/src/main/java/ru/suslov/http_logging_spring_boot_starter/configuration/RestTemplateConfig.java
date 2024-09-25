package ru.suslov.http_logging_spring_boot_starter.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import ru.suslov.http_logging_spring_boot_starter.controller.RestTemplateInterceptor;
import ru.suslov.http_logging_spring_boot_starter.service.HttpRequestLogService;

import java.util.Collections;

@Configuration
public class RestTemplateConfig {

    private final HttpRequestLogService httpRequestLogService;

    @Autowired
    public RestTemplateConfig(HttpRequestLogService httpRequestLogService) {
        this.httpRequestLogService = httpRequestLogService;
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        restTemplate.setInterceptors(Collections.singletonList(new RestTemplateInterceptor(httpRequestLogService)));

        return restTemplate;
    }
}
