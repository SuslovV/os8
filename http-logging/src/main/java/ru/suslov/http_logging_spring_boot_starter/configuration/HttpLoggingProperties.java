package ru.suslov.http_logging_spring_boot_starter.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "http-logging")
public class HttpLoggingProperties {

    private Boolean enabled;

}
