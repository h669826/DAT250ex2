package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.UUID;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class VoteOption {
    private UUID id = UUID.randomUUID();
    private String caption;
    private int presentationOrder;

    @JsonIdentityReference(alwaysAsId = true)
    private Poll poll;

    public VoteOption(){}

    public VoteOption(String caption) {
        this.caption = caption;
        this.presentationOrder = 0;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public UUID getId() {
        return id;
    }
    public String getCaption() {
        return caption;
    }
    public void setCaption(String caption) {
        this.caption = caption;
    }
    public int getPresentationOrder() {
        return presentationOrder;
    }
    public void setPresentationOrder(int presentationOrder) {
        this.presentationOrder = presentationOrder;
    }
    public void setPoll(Poll poll) {
        this.poll = poll;
    }
}
/*
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
    private Poll poll;

    public VoteOption() {}

    @Id
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    @Column(nullable = false)
    public String getCaption() { return caption; }
    public void setCaption(String caption) { this.caption = caption; }

    @Column(name = "presentation_order", nullable = false)
    public int getPresentationOrder() { return presentationOrder; }
    public void setPresentationOrder(int presentationOrder) { this.presentationOrder = presentationOrder; }

    @ManyToOne(optional = false)
    @JoinColumn(name = "poll_id", nullable = false)
    public Poll getPoll() { return poll; }
    public void setPoll(Poll poll) { this.poll = poll; }

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
*/

