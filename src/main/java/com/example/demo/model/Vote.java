package com.example.demo.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity
@Table(name = "votes")
@Access(AccessType.FIELD)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false,  fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "vote_option_id")
    @JsonIgnore
    private VoteOption voteOption;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poll_id", nullable = false)
    private Poll poll;

    @Column(name = "published_at", nullable = true)
    private Instant publishedAt = Instant.now();

    public Vote() {}


    @Transient
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    @Transient
    @JsonIgnore
    public User getUser(){
        return user;
    }
    public void setUser(User u) {
        this.user = u;
        if (u != null) {
            List<Vote> vs = u.getVotes();
            boolean already = false;
            for (Vote v : vs) { if (v == this) { already = true; break; } }
            if (!already) vs.add(this);
        }
    }

    @JsonIgnore
    public VoteOption getVoteOption() { return voteOption; }

    public Instant getPublishedAt() { return publishedAt; }
    public void setPublishedAt(Instant publishedAt) { this.publishedAt = publishedAt; }

    public void setVoteOption(VoteOption opt) {
        this.voteOption = opt;
    }

    @JsonProperty("voteOption")
    public String getVoteOptionIdForJson() {
        return (voteOption != null) ? voteOption.getId().toString() : null;
    }

    @JsonProperty("user")
    public String getUserIdForJson() {
        return (user != null) ? user.getId().toString() : null;
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

    public Poll getPoll() {
        return poll;
    }
    public void setPoll(Poll poll) {
        this.poll = poll;
    }
}



