package com.example.demo.messaging.events;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record PollUpdateEvent(UUID pollId,
                              Map<UUID, Long> tallies,
                              Instant publishedAt) {}
