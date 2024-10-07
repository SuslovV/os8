package ru.suslov.metrics_consumer.service;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.suslov.metrics_consumer.dto.MetricDto;
import ru.suslov.metrics_consumer.model.Metric;
import ru.suslov.metrics_consumer.repository.MetricRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class MetricService {
    private final MetricRepository metricRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public MetricService(MetricRepository metricRepository, ModelMapper modelMapper) {
        this.metricRepository = metricRepository;
        this.modelMapper = modelMapper;
    }

    public Optional<Metric> findById(UUID id) {
        return metricRepository.findById(id);
    }

    public List<Metric> findByApplicationName(String applicationName) {
        return metricRepository.findByApplicationName(applicationName);
    }

    public Page<Metric> findAll(Pageable pageable) {
        return metricRepository.findAll(pageable);
    }

    public Metric save(Metric metric) {
        return metricRepository.save(metric);
    }

    public Metric save(MetricDto metricDto) {
        Metric metric = modelMapper.map(metricDto, Metric.class);
        metric.setCreatedTime(OffsetDateTime.now());
        metric.setLastModifiedTime(metric.getCreatedTime());
        return metricRepository.save(metric);
    }

    public void delete(Metric metric) {
        metricRepository.delete(metric);
    }

}
