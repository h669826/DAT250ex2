package com.example.demo.messaging;

import com.example.demo.Services.PollService;
import com.example.demo.dto.BrokerVoteEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BrokerVoteListener {

    private final PollService pollService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "polls.all.votes.tmp", durable = "false", autoDelete = "true"),
            exchange = @Exchange(value = "polls", type = "topic"),
            key = "poll.*.votes"
    ))
    public void onVoteMessage(BrokerVoteEvent event) {
        pollService.addVoteFromBroker(event.pollId(), event.optionId(), event.userId(), event.publishedAt());
    }
}

