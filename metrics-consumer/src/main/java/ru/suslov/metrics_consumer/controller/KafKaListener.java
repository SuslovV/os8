package ru.suslov.metrics_consumer.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.suslov.metrics_consumer.dto.MetricDto;
import ru.suslov.metrics_consumer.service.MetricService;

@Component
//@Transactional to do
@Slf4j
public class KafKaListener {

    private final MetricService metricService;

    @Autowired
    public KafKaListener(MetricService metricService) {
        this.metricService = metricService;
    }

    @KafkaListener(topics = {"metrics-topic"}, containerFactory = "metricKafkaListenerContainerFactory", groupId = "metric-group")
    public void consume(MetricDto metricDto) {
        log.info("Kafka metrics consumer");
        metricService.save(metricDto);
    }
}
