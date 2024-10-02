package ru.suslov.user_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.suslov.user_service.model.RefreshToken;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    Optional<RefreshToken> findById(UUID id);
    Optional<RefreshToken> findByValue(UUID value);
}
