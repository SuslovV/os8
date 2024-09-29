package ru.suslov.user_service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import ru.suslov.user_service.model.Role;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

public class JpaConverterJsonSetOfString implements AttributeConverter<Set<Role>, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Set<Role> meta) {
        try {
            return objectMapper.writeValueAsString(meta);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public Set<Role> convertToEntityAttribute(String dbData) {
        if (dbData == null) return Collections.emptySet();
        try {
            return objectMapper.readValue(dbData, objectMapper.getTypeFactory().constructCollectionType(Set.class, Role.class));
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
