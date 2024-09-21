package ru.suslov.http_logging.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.OffsetDateTime;
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
    @JoinColumn(name = "method_source_code_id")
    private MethodSourceCode methodSourceCode;

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
    private UUID httpRequestId;

    @Column(columnDefinition = "timestamp with time zone")
    private OffsetDateTime createdTime;

    @Column(columnDefinition = "timestamp with time zone")
    private OffsetDateTime lastModifiedTime;

}
