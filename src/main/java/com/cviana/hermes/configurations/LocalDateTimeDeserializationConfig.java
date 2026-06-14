package com.cviana.hermes.configurations;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

@Configuration
public class LocalDateTimeDeserializationConfig extends JsonDeserializer<LocalDateTime> {
    private final DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    @Override
    public LocalDateTime deserialize(JsonParser parser, DeserializationContext context) throws IOException, JacksonException {
        String date = parser.getText();
        return LocalDateTime.parse(date, format);
    }

}
