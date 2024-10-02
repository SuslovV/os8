package ru.suslov.user_service.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table
@Data
@RequiredArgsConstructor
public class RefreshToken implements Serializable {

    @Id
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    private UUID id;
    @Column
    private UUID userId; // todo OneToOne
    @Column
    private UUID value;

    @Column
    private Instant expiryDate;

    @Column(columnDefinition = "timestamp with time zone")
    private OffsetDateTime createdTime;

    @Column(columnDefinition = "timestamp with time zone")
    private OffsetDateTime lastModifiedTime;

}
