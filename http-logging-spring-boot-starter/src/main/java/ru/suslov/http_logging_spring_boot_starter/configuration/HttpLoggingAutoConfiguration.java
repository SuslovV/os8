package ru.suslov.http_logging_spring_boot_starter.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import ru.suslov.http_logging_spring_boot_starter.controller.RequestResponseLoggingFilter;
import ru.suslov.http_logging_spring_boot_starter.repository.HttpRequestLogRepository;
import ru.suslov.http_logging_spring_boot_starter.service.HttpRequestLogService;
import ru.suslov.http_logging_spring_boot_starter.service.ResourceService;
import ru.suslov.http_logging_spring_boot_starter.service.ServerService;

@AutoConfiguration
@EnableConfigurationProperties(HttpLoggingProperties.class)
@AutoConfigureBefore(JpaRepositoriesAutoConfiguration.class)
@EnableJpaRepositories(basePackages = {"ru.suslov.http_logging_spring_boot_starter.repository"})
@EntityScan(basePackages = {"ru.suslov.http_logging_spring_boot_starter.model"})
@ComponentScan()
@ConditionalOnProperty(prefix = "http-logging", value = "enabled", havingValue = "true", matchIfMissing = false)
public class HttpLoggingAutoConfiguration {

    private HttpRequestLogRepository httpRequestLogRepository;

    @Bean
    @ConditionalOnMissingBean
    public RequestResponseLoggingFilter requestResponseLoggingFilter() {
        return new RequestResponseLoggingFilter();
    }

    @Bean
    @ConditionalOnMissingBean
    public HttpRequestLogService httpRequestLogService() {
        return new HttpRequestLogService();
    }

    @Bean
    @ConditionalOnMissingBean
    public ServerService serverService() {
        return new ServerService();
    }

    @Bean
    @ConditionalOnMissingBean
    public ResourceService resourceService() {
        return new ResourceService();
    }

}
