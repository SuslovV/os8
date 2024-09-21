package ru.suslov.perfomance_statistics.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.suslov.perfomance_statistics.model.ClassSourceCode;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClassSourceCodeRepository extends JpaRepository<ClassSourceCode, UUID> {
    Optional<ClassSourceCode> findById(UUID id);
    Optional<ClassSourceCode> findByName(String name);
}
