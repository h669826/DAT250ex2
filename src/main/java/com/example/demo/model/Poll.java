package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Poll {
    private UUID id = UUID.randomUUID();
    private String question;
    private Instant publishedAt;
    private Instant validUntil;
    private List<VoteOption> voteOptions;

    @JsonIdentityReference(alwaysAsId = true)
    private User creator;

    public Poll(){}

    public Poll(String question, Instant publishedAt, Instant validUntil, List<VoteOption> voteOptions, User creator) {
        this.question = question;
        this.publishedAt = publishedAt;
        this.validUntil = validUntil;
        this.voteOptions = voteOptions;
        this.creator = creator;
    }
    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public String getQuestion() {
        return question;
    }
    public void setQuestion(String question) {
        this.question = question;
    }
    public Instant getPublishedAt() {
        return publishedAt;
    }
    public void setPublishedAt(Instant publishedAt) {
        this.publishedAt = publishedAt;
    }

    public Instant getValidUntil() {
        return validUntil;
    }
    public void setValidUntil(Instant validUntil) {
        this.validUntil = validUntil;
    }
    public List<VoteOption> getVoteOptions() {
        return voteOptions;
    }
    public void setVoteOptions(List<VoteOption> voteOptions) {
        this.voteOptions = voteOptions;
    }
    public User getCreator() {
        return creator;
    }
    public void setCreator(User creator) {
        this.creator = creator;
    }
}
