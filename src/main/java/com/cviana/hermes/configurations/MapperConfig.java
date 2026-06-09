package com.cviana.hermes.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class MapperConfig {

    @Bean
    public ObjectMapper mapper() {
        return new ObjectMapper();
    }
}
