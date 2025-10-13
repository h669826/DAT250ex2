package com.example.demo.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

// PollRepo.java
public interface PollRepo extends JpaRepository<Poll, UUID> {
    List<Poll> findByUserId(UUID userId);
}

