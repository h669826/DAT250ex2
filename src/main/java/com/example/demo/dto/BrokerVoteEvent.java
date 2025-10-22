package com.example.demo.dto;

import java.time.Instant;
import java.util.UUID;

public record BrokerVoteEvent(
        UUID pollId,
        UUID optionId,
        UUID userId,
        Instant publishedAt
) {}

