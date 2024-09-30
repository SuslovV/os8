package ru.suslov.user_service.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Data
public class UserAppPrincipal implements Principal {

    private final UUID id;
    private final String name;
    private final List<String> roles;

    @Override
    public String getName() {
        return name;
    }
}
