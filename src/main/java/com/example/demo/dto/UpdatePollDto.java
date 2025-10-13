package com.example.demo.dto;

import java.util.List;
import java.util.UUID;

public record UpdatePollDto(
        String question,
        List<OptionPatch> options
) {
    public record OptionPatch(
            UUID id,
            String text,
            Boolean remove
    ) {}
}

