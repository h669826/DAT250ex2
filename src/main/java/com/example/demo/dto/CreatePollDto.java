package com.example.demo.dto;

import com.example.demo.model.VoteOption;

import java.time.Instant;
import java.util.List;

public class CreatePollDto {
    private String question;
    private java.time.Instant publishedAt;
    private java.time.Instant validUntil;
    private List<OptionDto> voteOptions;

    public String getQuestion() {
        return question;
    }
    public void setQuestion(String question) {
        this.question = question;
    }
    public java.time.Instant getPublishedAt() {
        return publishedAt;
    }
    public void setPublishedAt(java.time.Instant publishedAt) {
        this.publishedAt = publishedAt;
    }
    public java.time.Instant getValidUntil() {
        return validUntil;
    }
    public void setValidUntil(java.time.Instant validUntil) {
        this.validUntil = validUntil;
    }
    public List<OptionDto> getVoteOptions() {
        return voteOptions;
    }
    public void setVoteOptions(List<OptionDto> voteOptions) {
        this.voteOptions = voteOptions;
    }

    public static class OptionDto {
        private String caption;
        private int presentationOrder;

        public int getPresentationOrder() {
            return presentationOrder;
        }
        public void setPresentationOrder(int presentationOrder) {
            this.presentationOrder = presentationOrder;
        }
        public String getCaption() {
            return caption;
        }
        public void setCaption(String caption) {
            this.caption = caption;
        }
    }
}
