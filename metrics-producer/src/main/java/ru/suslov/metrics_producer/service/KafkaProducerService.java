package ru.suslov.metrics_producer.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.suslov.metrics_producer.dto.MetricDto;

@Service
@Slf4j
public class KafkaProducerService {

    private final KafkaTemplate<String, MetricDto> kafkaTemplate;

    @Autowired
    public KafkaProducerService(KafkaTemplate<String, MetricDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(String topicName, MetricDto metricDto) {
        kafkaTemplate.send(topicName, "1", metricDto);
    }
}
