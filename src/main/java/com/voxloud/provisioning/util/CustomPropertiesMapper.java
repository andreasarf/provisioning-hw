package com.voxloud.provisioning.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CustomPropertiesMapper {

    private final ObjectMapper objectMapper;

    public <T> String serialize(T object) {
        final var objectMap = objectMapper.convertValue(object, new TypeReference<Map<String, Object>>() {});
        final var sb = new StringBuilder();

        for (final var entry : objectMap.entrySet()) {
            sb.append(entry.getKey()).append("=");

            if (entry.getValue() instanceof List) {
                for (Object value : (List<?>) entry.getValue()) {
                    sb.append(value).append(",");
                }
                sb.deleteCharAt(sb.length() - 1); // Remove the last comma
            } else {
                sb.append(entry.getValue());
            }

            sb.append("\n");
        }

        return sb.toString();
    }

    public <T> T deserialize(String json, Class<T> clazz) {
        final var objectMap = new HashMap<String, Object>();

        for (String line : json.split("\n")) {
            if (line.contains("=")) {
                String[] parts = line.split("=");
                String key = parts[0].trim();
                String value = parts[1].trim();
                objectMap.put(key, value);
            } else {
                throw new IllegalArgumentException("invalid format: " + line);
            }
        }

        return objectMapper.convertValue(objectMap, clazz);
    }
}
