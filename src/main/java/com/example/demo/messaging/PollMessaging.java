package com.example.demo.messaging;

import com.example.demo.messaging.events.PollUpdateEvent;
import com.example.demo.messaging.events.VoteEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@ConditionalOnProperty(name = "messaging.enabled", havingValue = "true")
@RequiredArgsConstructor
public class PollMessaging {
    private final AmqpAdmin amqp;
    private final TopicExchange exchange;
    private final RabbitTemplate rabbit;

    public void registerPollTopic(UUID pollId) {
        String qName = "poll." + pollId;
        String rkUpdates = "poll." + pollId;
        amqp.declareQueue(new Queue(qName, false, false, true));
        amqp.declareBinding(BindingBuilder.bind(new Queue(qName))
                .to(exchange).with(rkUpdates));
    }

    public void publishUpdate(PollUpdateEvent evt) {
        rabbit.convertAndSend(MessagingConfig.EXCHANGE, "poll." + evt.pollId(), evt);
    }

    public void publishVote(VoteEvent evt) {
        rabbit.convertAndSend(MessagingConfig.EXCHANGE, "poll." + evt.pollId() + ".vote", evt);
    }
}
