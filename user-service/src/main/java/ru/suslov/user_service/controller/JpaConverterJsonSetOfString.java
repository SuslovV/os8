package ru.suslov.user_service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

public class JpaConverterJsonSetOfString<T> implements AttributeConverter<Set<T>, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Set<T> meta) {
        try {
            return objectMapper.writeValueAsString(meta);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public Set<T> convertToEntityAttribute(String dbData) {
        if (dbData == null) return Collections.emptySet();
        try {
            return objectMapper.readValue(dbData, Set.class);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
