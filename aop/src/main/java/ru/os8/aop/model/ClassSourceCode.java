package ru.os8.aop.model;

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
public class ClassSourceCode {

    @Id
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(length = 200)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "classSourceCode", cascade = CascadeType.DETACH, orphanRemoval = true)
    @BatchSize(size = 100)
    private Set<Method> methods;

    @Column(columnDefinition = "timestamp with time zone")
    private OffsetDateTime createdTime;

    @Column(columnDefinition = "timestamp with time zone")
    private OffsetDateTime lastModifiedTime;

    @Column()
    private Boolean active;

    @Column()
    private Boolean deleted;

}