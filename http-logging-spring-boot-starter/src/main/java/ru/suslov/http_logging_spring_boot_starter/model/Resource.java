package ru.suslov.http_logging_spring_boot_starter.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.UuidGenerator;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(indexes = {
        @Index(name = "name_server_id_index", columnList = "name, server_id", unique = true)
})
@Data
@RequiredArgsConstructor
@BatchSize(size = 100)
public class Resource {

    @Id
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(length = 200)
    private String name;

    @Column(length = 200)
    private String path;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "server_id")
    private Server server;

    @Column(columnDefinition = "timestamp with time zone")
    private OffsetDateTime createdTime;

    @Column(columnDefinition = "timestamp with time zone")
    private OffsetDateTime lastModifiedTime;

    @Column()
    private Boolean active;

    @Column()
    private Boolean deleted;

}