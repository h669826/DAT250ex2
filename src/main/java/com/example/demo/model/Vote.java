package com.example.demo.model;

import java.time.Instant;

public class Vote {
    private Instant publishedAt;
    private VoteOption voteOption;

    public Vote(){}

    public Vote(Instant publishedAt, VoteOption voteOption) {
        this.publishedAt = publishedAt;
        this.voteOption = voteOption;
    }
    public Instant getPublishedAt() {
        return publishedAt;
    }
    public VoteOption getVoteOption() {
        return voteOption;
    }
}
