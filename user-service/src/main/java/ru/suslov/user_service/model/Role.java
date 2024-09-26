package ru.suslov.user_service.model;

public enum Role {
    ADMIN(1, "ADMIN"),
    USER(2, "USER"),
    ;

    private final int key;
    private final String description;

    private Role(int key, String description) {
        this.key = key;
        this.description = description;
    }

}
