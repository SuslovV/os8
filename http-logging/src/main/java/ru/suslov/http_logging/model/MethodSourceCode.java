package ru.suslov.http_logging.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.UuidGenerator;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "method", indexes = {
        @Index(name = "nameIndex", columnList = "name, classSourceCode", unique = true)
})
@Data
@RequiredArgsConstructor
@BatchSize(size = 100)
public class MethodSourceCode {

    @Id
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(length = 200)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "class_source_code_id")
    private ClassSourceCode classSourceCode;

    @Column(columnDefinition = "timestamp with time zone")
    private OffsetDateTime createdTime;

    @Column(columnDefinition = "timestamp with time zone")
    private OffsetDateTime lastModifiedTime;

    @Column()
    private Boolean active;

    @Column()
    private Boolean deleted;

}