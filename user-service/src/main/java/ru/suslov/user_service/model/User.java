package ru.suslov.user_service.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import ru.suslov.user_service.controller.JpaConverterJsonSetOfString;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(indexes = {
        @Index(name = "user_name_index", columnList = "username", unique = true)
})
@Data
@RequiredArgsConstructor
public class User {

    @Id
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column
    private String firstName;
    @Column
    private String secondName;
    @Column
    private String username;
    @Column
    private String email;
    @Column
    private String password;

    @Convert(converter = JpaConverterJsonSetOfString.class)
    @Column(length = 65535)
    private Set<Role> roles;

    @Column(columnDefinition = "timestamp with time zone")
    private OffsetDateTime createdTime;

    @Column(columnDefinition = "timestamp with time zone")
    private OffsetDateTime lastModifiedTime;

    @Column()
    private Boolean active;

    @Column()
    private Boolean deleted;

}
