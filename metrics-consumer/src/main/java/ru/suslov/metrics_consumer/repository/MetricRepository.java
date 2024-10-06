package ru.suslov.metrics_consumer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.suslov.metrics_consumer.model.Metric;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MetricRepository extends JpaRepository<Metric, UUID> {
    Optional<Metric> findById(UUID id);

}
