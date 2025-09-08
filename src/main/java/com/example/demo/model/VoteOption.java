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

    public VoteOption(String caption, int presentationOrder) {
        this.caption = caption;
        this.presentationOrder = presentationOrder;
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
