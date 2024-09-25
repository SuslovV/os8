package ru.suslov.http_logging_spring_boot_starter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.suslov.http_logging_spring_boot_starter.model.HttpRequestLog;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface HttpRequestLogRepository extends JpaRepository<HttpRequestLog, UUID> {
    Optional<HttpRequestLog> findById(UUID id);

}
