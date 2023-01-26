package org.hibernate.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;

import java.io.IOException;
import java.util.HashMap;

public class HashMapConverter implements AttributeConverter<HashMap<String, Object>, String> {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(HashMap<String, Object> stringObjectHashMap) {
        try {
            return mapper.writeValueAsString(stringObjectHashMap);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public HashMap<String, Object> convertToEntityAttribute(String string) {
        try {
            return mapper.readValue(string, new TypeReference<>() {});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
