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
/*
import jakarta.persistence.*;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity
@Table(name = "votes")
@Access(AccessType.PROPERTY)
public class Vote {

    private UUID id = UUID.randomUUID();

    private User user;
    private VoteOption option;

    private Instant publishedAt = Instant.now();

    public Vote() {}

    @Id
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    @ManyToOne(optional = false)
    @JoinColumn(name = "voted_by_id", nullable = false)
    public User getVotedBy() { return user; }
    public void setVotedBy(User u) {
        this.user = u;
        if (u != null) {
            List<Vote> vs = u.getVotes();
            boolean already = false;
            for (Vote v : vs) { if (v == this) { already = true; break; } }
            if (!already) vs.add(this);
        }
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "vote_option_id", nullable = false)
    public VoteOption getVotesOn() { return option; }
    public void setVotesOn(VoteOption option) { this.option = option; }

    @Column(name = "published_at", nullable = false)
    public Instant getPublishedAt() { return publishedAt; }
    public void setPublishedAt(Instant publishedAt) { this.publishedAt = publishedAt; }

    @Transient
    public User getUser() { return user; }
    public void setUser(User u) {
        setVotedBy(u);
    }

    @Transient
    public VoteOption getOption() { return option; }
    public void setOption(VoteOption o) {
        setVotesOn(o);
    }

    public void setVoteOption(VoteOption opt) {
        this.option = opt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vote other)) return false;
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

}*/



