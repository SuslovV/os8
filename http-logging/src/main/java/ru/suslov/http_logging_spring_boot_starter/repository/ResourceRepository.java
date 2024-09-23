package ru.suslov.http_logging_spring_boot_starter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.suslov.http_logging_spring_boot_starter.model.Server;
import ru.suslov.http_logging_spring_boot_starter.model.Resource;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, UUID> {
    Optional<Resource> findById(UUID id);
    Optional<Resource> findByNameAndServer(String name, Server server);
}
