package ru.suslov.user_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.suslov.user_service.model.UserApp;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserAppRepository extends JpaRepository<UserApp, UUID> {
    Optional<UserApp> findById(UUID id);
    Optional<UserApp> findByUsername(String name);
    Optional<UserApp> findByEmail(String email);
}
