package ru.suslov.http_logging_spring_boot_starter.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.UuidGenerator;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(indexes = {
        @Index(name = "nameIndex", columnList = "name", unique = true)
})
@Data
@RequiredArgsConstructor
public class Server {

    @Id
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(length = 200)
    private String name;

    @Column(length = 200)
    private String path;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "server", cascade = CascadeType.DETACH, orphanRemoval = true)
    @BatchSize(size = 100)
    private Set<Resource> resources;

    @Column(columnDefinition = "timestamp with time zone")
    private OffsetDateTime createdTime;

    @Column(columnDefinition = "timestamp with time zone")
    private OffsetDateTime lastModifiedTime;

    @Column()
    private Boolean active;

    @Column()
    private Boolean deleted;

}
