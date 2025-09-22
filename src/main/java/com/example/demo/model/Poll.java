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
}/*
import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity
@Table(name = "polls")
@Access(AccessType.PROPERTY)
public class Poll {

    private UUID id = UUID.randomUUID();
    private String question;
    private User creator; // keep original field name
    private List<VoteOption> voteOptions = new ArrayList<>();

    private Instant publishedAt = Instant.now();

    public Poll() {}

    @Id
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    @Column(nullable = false)
    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }

    @ManyToOne(optional = false)
    @JoinColumn(name = "created_by_id", nullable = false)
    public User getCreatedBy() { return creator; }
    public void setCreatedBy(User u) {
        this.creator = u;
        if (u != null) {
            var created = u.getCreated();
            if (!created.contains(this)) created.add(this);
        }
    }

    @OneToMany(mappedBy = "poll", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("presentationOrder ASC")
    public List<VoteOption> getOptions() {
        if (voteOptions == null) voteOptions = new ArrayList<>();
        return voteOptions;
    }
    public void setOptions(List<VoteOption> opts) { this.voteOptions = opts; }

    @Column(name = "published_at", nullable = false)
    public Instant getPublishedAt() { return publishedAt; }
    public void setPublishedAt(Instant publishedAt) { this.publishedAt = publishedAt; }

    public VoteOption addVoteOption(String caption) {
        VoteOption opt = new VoteOption();
        opt.setCaption(caption);
        opt.setPresentationOrder(voteOptions.size());
        opt.setPoll(this);
        voteOptions.add(opt);
        return opt;
    }

    @Transient
    public User getCreator() { return creator; }
    public void setCreator(User creator) {
        setCreatedBy(creator);
    }

    @Transient
    public List<VoteOption> getVoteOptions() { return voteOptions; }
    public void setVoteOptions(List<VoteOption> voteOptions) { this.voteOptions = voteOptions; }

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




