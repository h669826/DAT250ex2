package com.example.demo.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity
@Table(name = "polls")
@Access(AccessType.PROPERTY)
public class Poll {

    private UUID id = UUID.randomUUID();
    private String question;

    @ManyToOne(optional = false)
    @JoinColumn(name = "created_by_id", nullable = false)
    private User creator;

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
    @JsonIgnore
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
    @JsonIgnore
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
    @JsonProperty("creator")
    @JsonIdentityReference(alwaysAsId = true)
    public User getCreator() { return creator; }
    @JsonProperty("creator")
    @JsonIdentityReference(alwaysAsId = true)
    public void setCreator(User creator) {
        setCreatedBy(creator);
    }

    @Transient
    @JsonProperty("createdBy")
    @JsonIdentityReference(alwaysAsId = true)
    public User getCreatedByJson() { return creator; }
    @JsonProperty("createdBy")
    @JsonIdentityReference(alwaysAsId = true)
    public void setCreatedByJson(User u) { setCreatedBy(u); }

    @Transient
    @JsonProperty("voteOptions")
    public List<VoteOption> getVoteOptions() { return voteOptions; }
    public void setVoteOptions(List<VoteOption> voteOptions) { this.voteOptions = voteOptions; }

    @Transient
    @JsonProperty("options")
    public List<VoteOption> getOptionsJson() { return voteOptions; }
    @JsonProperty("options")
    public void setOptionsJson(List<VoteOption> opts) { this.voteOptions = opts; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Poll other)) return false;
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

}




