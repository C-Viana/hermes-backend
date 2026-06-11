package com.cviana.hermes.configs;

import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestRestTemplateTestConfiguration {

    @Bean
    public TestRestTemplate testRestTemplate() {
        var restTemplate = new RestTemplateBuilder().rootUri("http://localhost:8080");
        return new TestRestTemplate(restTemplate);
    }
}
