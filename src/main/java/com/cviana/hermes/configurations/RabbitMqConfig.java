package com.cviana.hermes.configurations;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    public static final String QUEUE_NAME = "notifications.v1.send";
    public static final String EXCHANGE_NAME = "notifications";
    public static final String ROUTING_KEY = "pending";

    public static final String DLQ_QUEUE_NAME = "notifications.v1.dlq";
    public static final String DLQ_EXCHANGE_NAME = "notifications.dlq";
    public static final String DLQ_ROUTING_KEY = "dlq";

    @Bean
    public Queue deadLetterQueue() {
        return new Queue(DLQ_QUEUE_NAME);
    }

    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(DLQ_EXCHANGE_NAME);
    }

    @Bean
    public Binding deadLetterBinding() {
        return BindingBuilder.bind(deadLetterQueue()).to(deadLetterExchange()).with(DLQ_ROUTING_KEY);
    }

    // ----------------------------------------------

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue mainQueue() {
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("x-dead-letter-exchange", DLQ_EXCHANGE_NAME);
        args.put("x-dead-letter-routing-key", DLQ_ROUTING_KEY);
        
        return QueueBuilder.durable(QUEUE_NAME)
            .withArguments(args)
            .build();
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(mainQueue()).to(exchange()).with(ROUTING_KEY);
    }
}
