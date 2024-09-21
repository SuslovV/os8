package ru.suslov.perfomance_statistics.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.suslov.perfomance_statistics.model.ClassSourceCode;
import ru.suslov.perfomance_statistics.model.Method;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MethodRepository extends JpaRepository<Method, UUID> {
    Optional<Method> findById(UUID id);
    Optional<Method> findByNameAndClassSourceCode(String name, ClassSourceCode classSourceCode);
}
