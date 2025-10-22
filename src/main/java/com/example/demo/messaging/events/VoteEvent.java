package com.example.demo.messaging.events;

import java.time.Instant;
import java.util.UUID;

public record VoteEvent(UUID pollId, UUID optionId, UUID userId, Instant publishedAt) {}
