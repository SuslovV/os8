package ru.suslov.http_logging_spring_boot_starter.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

@Entity
@Table()
@Data
@RequiredArgsConstructor
public class HttpRequestLog {

    @Id
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "resource_id")
    private Resource resource;

    @Column
    private Long executionTime;

//    @Column
//    todo переименовать
//    private TypeRequest typeRequest; / input output

    @Column
//    todo переименовать
    private boolean response;

    @Column
    private String method;

    @Column
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, String> headers;

    @Column
    private UUID httpRequestId;

    @Column(columnDefinition = "timestamp with time zone")
    private OffsetDateTime createdTime;

    @Column(columnDefinition = "timestamp with time zone")
    private OffsetDateTime lastModifiedTime;

}
