package com.example.demo.messaging;


import com.example.demo.Services.PollService;
import com.example.demo.messaging.events.PollUpdateEvent;
import com.example.demo.messaging.events.VoteEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@ConditionalOnProperty(name = "messaging.enabled", havingValue = "true")
@RequiredArgsConstructor
public class VoteListener {
    private final PollService pollService;
    private final PollMessaging messaging;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "polls.consumer", durable = "false", autoDelete = "true"),
            exchange = @Exchange(value = MessagingConfig.EXCHANGE, type = "topic"),
            key = "poll.*.vote"
    ))
    public void onVote(VoteEvent evt) {
        pollService.addVoteFromBroker(evt.pollId(), evt.optionId(), evt.userId(), evt.publishedAt());
        var tallies = pollService.getTallies(evt.pollId());
        messaging.publishUpdate(new PollUpdateEvent(evt.pollId(), tallies, Instant.now()));
    }
}
