package com.example.demo.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity
@Table(name = "votes")
@Access(AccessType.PROPERTY)
public class Vote {

    private UUID id = UUID.randomUUID();

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "option_id")
    private VoteOption option;

    @Column(nullable = true)
    private Instant publishedAt = Instant.now();

    public Vote() {}

    @Id
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    @ManyToOne(optional = false)
    @JsonIgnore
    public User getVotedBy() { return user; }

    @Transient
    @JsonProperty("user")
    @JsonIdentityReference(alwaysAsId = true)
    public User getUser(){
        return user;
    }
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
    @JsonIgnore
    public VoteOption getVoteOption() { return option; }
    public void setVotesOn(VoteOption option) { this.option = option; }

    @Column(name = "published_at", nullable = false)
    public Instant getPublishedAt() { return publishedAt; }
    public void setPublishedAt(Instant publishedAt) { this.publishedAt = publishedAt; }

    @JsonProperty("user")
    @JsonIdentityReference(alwaysAsId = true)
    public void setUser(User u) {
        setVotedBy(u);
    }

    @Transient
    @JsonProperty("voteOption")
    @JsonIdentityReference(alwaysAsId = true)
    public VoteOption getOption() { return option; }
    public void setOption(VoteOption o) {
        setVotesOn(o);
    }

    @JsonProperty("voteOption")
    @JsonIdentityReference(alwaysAsId = true)
    public void setVoteOption(VoteOption opt) {
        this.option = opt;
    }

    @Transient
    @JsonProperty("option")
    @JsonIdentityReference(alwaysAsId = true)
    public VoteOption getOptionAlias() { return option; }
    @JsonProperty("option")
    @JsonIdentityReference(alwaysAsId = true)
    public void setOptionAlias(VoteOption opt) { setVotesOn(opt); }

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

}



