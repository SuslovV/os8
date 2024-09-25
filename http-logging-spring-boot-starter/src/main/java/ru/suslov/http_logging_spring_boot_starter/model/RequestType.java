package ru.suslov.http_logging_spring_boot_starter.model;

public enum RequestType {
    INCOMING(1, "INCOMING"),
    OUTGOING(2, "OUTGOING"),
    ;

    private final int key;
    private final String description;

    private RequestType(int key, String description) {
        this.key = key;
        this.description = description;
    }

}