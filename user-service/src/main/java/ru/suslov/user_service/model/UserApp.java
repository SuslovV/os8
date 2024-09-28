package ru.suslov.user_service.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ru.suslov.user_service.controller.JpaConverterJsonSetOfString;

import java.time.OffsetDateTime;
import java.util.*;

@Entity
@Table(indexes = {
        @Index(name = "user_name_index", columnList = "username", unique = true),
        @Index(name = "email_index", columnList = "email", unique = true)
})
@Data
@RequiredArgsConstructor
public class UserApp {

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

    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        this.roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role.name())));
        return authorities;
    }

    @Column(columnDefinition = "timestamp with time zone")
    private OffsetDateTime createdTime;

    @Column(columnDefinition = "timestamp with time zone")
    private OffsetDateTime lastModifiedTime;

    @Column()
    private Boolean active;

    @Column()
    private Boolean deleted;

}
