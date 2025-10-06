package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity
@Table(name = "vote_options")
@Access(AccessType.PROPERTY)
public class VoteOption {

    private UUID id = UUID.randomUUID();
    private String caption;
    private int presentationOrder;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "poll_id", nullable = false)
    private Poll poll;

    public VoteOption() {}

    @Id
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    @Column(nullable = false)
    public String getCaption() { return caption; }
    public void setCaption(String caption) { this.caption = caption; }

    @Column(name = "presentationOrder", nullable = false)
    public int getPresentationOrder() { return presentationOrder; }
    public void setPresentationOrder(int presentationOrder) { this.presentationOrder = presentationOrder; }

    @Transient
    @JsonProperty("order")
    public int getOrder() { return presentationOrder; }
    @JsonProperty("order")
    public void setOrder(int v) { this.presentationOrder = v; }

    @ManyToOne(optional = false)
    @JoinColumn(name = "poll_id", nullable = false)
    @JsonIgnore
    public Poll getPoll() { return poll; }
    public void setPoll(Poll poll) { this.poll = poll; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VoteOption other)) return false;
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

}


