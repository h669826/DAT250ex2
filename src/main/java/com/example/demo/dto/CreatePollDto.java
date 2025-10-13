package com.example.demo.dto;

import java.util.List;

public record CreatePollDto(
        String question,
        List<String> options
) {}
