package com.cviana.hermes;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.rabbitmq.RabbitMQContainer;

import com.cviana.hermes.notifications.NotificationRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Testcontainers
class TestcontainersConfiguration {

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:18");

    @Container
    @ServiceConnection
    static RabbitMQContainer rabbitmq = new RabbitMQContainer("rabbitmq:3-management");

    @SuppressWarnings({ "rawtypes", "resource" })
    @Container
    @ServiceConnection
    static GenericContainer<?> redis = new GenericContainer("redis:8").withExposedPorts(6379);

    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private NotificationRepository repository;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Test
    void deveEnviarNotificationParaAFilaERetornarAccepted() {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("type", "EMAIL");
        params.add("addressee", "joaosilva@gmail.com, mariaferreira@yahoo.com.br");
        params.add("message", "Olá Testcontainers");

        ResponseEntity<Void> response = testRestTemplate.postForEntity("/api/v1/notifications", params, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        assertThat(repository.count()).isEqualTo(1);
        assertThat(redisTemplate.getConnectionFactory().getConnection().serverCommands().dbSize()).isEqualTo(1L);
    }
}