package ru.suslov.http_logging_spring_boot_starter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.suslov.http_logging_spring_boot_starter.model.Server;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ServerRepository extends JpaRepository<Server, UUID> {
    Optional<Server> findById(UUID id);
    Optional<Server> findByName(String name);
}
