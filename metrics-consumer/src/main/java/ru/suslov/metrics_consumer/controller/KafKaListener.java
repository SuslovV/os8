package ru.suslov.metrics_consumer.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.suslov.metrics_consumer.dto.MetricDto;
import ru.suslov.metrics_consumer.service.MetricService;

@Component
@Slf4j
public class KafKaListener {

    private final MetricService metricService;

    @Autowired
    public KafKaListener(MetricService metricService) {
        this.metricService = metricService;
    }

    @KafkaListener(topics = {"metrics-topic"}, containerFactory = "metricKafkaListenerContainerFactory")
    public void consume(MetricDto metricDto) {
        metricService.save(metricDto);
    }
}
