package ru.suslov.http_logging.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.suslov.http_logging.model.ClassSourceCode;
import ru.suslov.http_logging.model.MethodSourceCode;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MethodSourceCodeRepository extends JpaRepository<MethodSourceCode, UUID> {
    Optional<MethodSourceCode> findById(UUID id);
    Optional<MethodSourceCode> findByNameAndClassSourceCode(String name, ClassSourceCode classSourceCode);
}
