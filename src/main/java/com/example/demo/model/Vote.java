package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.time.Instant;
import java.util.UUID;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Vote {
    private UUID id = UUID.randomUUID();

    @JsonIdentityReference(alwaysAsId = true)
    private User user;

    private Instant publishedAt;

    @JsonIdentityReference(alwaysAsId = true)
    private Poll poll;

    @JsonIdentityReference(alwaysAsId = true)
    private VoteOption voteOption;

    public Vote(){}

    public Vote(Instant publishedAt, VoteOption voteOption, User user) {
        this.publishedAt = publishedAt;
        this.voteOption = voteOption;
        this.user = user;
    }
    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public Instant getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Instant publishedAt) {
        this.publishedAt = publishedAt;
    }

    public VoteOption getVoteOption() {
        return voteOption;
    }
    public void setVoteOption(VoteOption voteOption) {
        this.voteOption = voteOption;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
}
