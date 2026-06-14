package com.cviana.hermes.configurations;

import java.time.LocalDateTime;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

@Configuration
public class MapperConfig {

    @Bean
    @Primary
    public ObjectMapper mapper() {
        ObjectMapper mapper = new ObjectMapper();
        JavaTimeModule module = new JavaTimeModule();
        module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializationConfig());
        module.addDeserializer(LocalDateTime.class, LocalDateTimeDeserializer.INSTANCE);
        mapper.registerModule(module);
        return mapper;
    }
}
