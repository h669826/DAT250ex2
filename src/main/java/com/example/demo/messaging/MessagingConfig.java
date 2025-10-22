package com.example.demo.messaging;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConditionalOnProperty(name = "messaging.enabled", havingValue = "true")
public class MessagingConfig {
    public static final String EXCHANGE = "polls";

    @Bean
    TopicExchange pollsExchange() { return new TopicExchange(EXCHANGE, true, false); }

    @Bean
    Jackson2JsonMessageConverter msgConverter() { return new Jackson2JsonMessageConverter(); }

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory cf, Jackson2JsonMessageConverter conv) {
        RabbitTemplate t = new RabbitTemplate(cf);
        t.setMessageConverter(conv);
        return t;
    }
}
