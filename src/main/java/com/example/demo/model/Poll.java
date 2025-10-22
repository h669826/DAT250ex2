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
@Access(AccessType.FIELD)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Poll {

    @Id
    @GeneratedValue
    @Column(nullable = false, updatable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;
    private String question;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "poll", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<VoteOption> voteOptions = new ArrayList<>();

    @Column(name = "published_at", nullable = false)
    private Instant publishedAt = Instant.now();

    @Column(name = "valid_until")
    private Instant validUntil;

    public Poll() {}

    @Transient
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }

    @Transient
    public User getUser() { return user; }
    public void setUser(User u) {
        this.user = u;
        if (u != null) {
            var created = u.getCreated();
            if (!created.contains(this)) created.add(this);
        }
    }

    public Instant getValidUntil() {
        return validUntil;
    }
    public void setValidUntil(Instant validUntil) {
        this.validUntil = validUntil;
    }

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

    public void addOption(VoteOption opt) {
        voteOptions.add(opt);
        opt.setPoll(this);
    }
    public void removeOption(VoteOption opt) {
        voteOptions.remove(opt);
        opt.setPoll(null);
    }

    @Transient
    @JsonProperty("user")
    @JsonIdentityReference(alwaysAsId = true)
    public User getCreator() { return user; }
    @JsonProperty("user")
    @JsonIdentityReference(alwaysAsId = true)
    public void setCreator(User user) {
        setUser(user);
    }

    @Transient
    @JsonProperty("voteOptions")
    public List<VoteOption> getVoteOptions() { return voteOptions; }
    public void setVoteOptions(List<VoteOption> voteOptions) {
        this.voteOptions.clear();
        if (voteOptions != null) this.voteOptions.addAll(voteOptions);
    }

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




