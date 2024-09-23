package ru.suslov.http_logging_spring_boot_starter.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.suslov.http_logging_spring_boot_starter.controller.RequestResponseLoggingFilter;
import ru.suslov.http_logging_spring_boot_starter.repository.HttpRequesLogtRepository;
import ru.suslov.http_logging_spring_boot_starter.repository.ResourceRepository;
import ru.suslov.http_logging_spring_boot_starter.repository.ServerRepository;
import ru.suslov.http_logging_spring_boot_starter.service.HttpRequestLogService;
import ru.suslov.http_logging_spring_boot_starter.service.ResourceService;
import ru.suslov.http_logging_spring_boot_starter.service.ServerService;

@AutoConfiguration
@EnableConfigurationProperties(HttpLoggingProperties.class)
@ConditionalOnProperty(prefix = "http-logging", value = "enabled", havingValue = "true", matchIfMissing = false)
public class HttpLoggingAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public RequestResponseLoggingFilter requestResponseLoggingFilter() {
        return new RequestResponseLoggingFilter();
    }

}
