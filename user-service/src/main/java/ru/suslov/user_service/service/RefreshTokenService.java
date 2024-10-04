package ru.suslov.user_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.suslov.user_service.exception.TokenRefreshException;
import ru.suslov.user_service.model.RefreshToken;
import ru.suslov.user_service.repository.RefreshTokenRepository;

import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Value("${jwt.refreshTokenExpiration}")
    private Duration jwtRefreshTokenExpiration;

    private final RefreshTokenRepository refreshTokenRepository;

    @Autowired
    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public Optional<RefreshToken> findById(UUID id) {
        return refreshTokenRepository.findById(id);
    }

    public Optional<RefreshToken> findByValue(UUID value) {
        return refreshTokenRepository.findByValue(value);
    }

    public RefreshToken save(RefreshToken refreshToken) {
        return refreshTokenRepository.save(refreshToken);
    }

    public void delete(RefreshToken refreshToken) {
        refreshTokenRepository.delete(refreshToken);
    }

    public void deleteAllByUserId(UUID userId) {
        refreshTokenRepository.deleteAllByUserId(userId);
    }

    public RefreshToken add(UUID userAppId) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUserId(userAppId);
        refreshToken.setValue(UUID.randomUUID());
        refreshToken.setExpiryDate(Instant.now().plusMillis(jwtRefreshTokenExpiration.toMillis()));
        refreshToken.setCreatedTime(OffsetDateTime.now());
        refreshToken.setLastModifiedTime(refreshToken.getCreatedTime());

        return refreshTokenRepository.save(refreshToken);
    }

    public Boolean verifyExpiration(RefreshToken refreshToken) {
        if (refreshToken.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(refreshToken);
            throw new TokenRefreshException("Refresh token was expired. Please make a new signin request.");
        }

        return true;
    }

}
