package ru.suslov.metrics_consumer.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.OffsetDateTime;
import java.util.Map;

@Data
@RequiredArgsConstructor
public class MetricDto {

    private String applicationName;
    private OffsetDateTime dateMetric;
    private Map<String, Object> value;

    public MetricDto(String applicationName, OffsetDateTime dateMetric, Map<String, Object> value) {
        this.applicationName = applicationName;
        this.dateMetric = dateMetric;
        this.value = value;
    }
}
