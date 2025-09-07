package com.example.demo.model;

import java.util.List;

public class User {
    private String username;
    private String email;
    private List<Poll> createdPolls;
    private List<Vote> votes;

    public User(){}

    public User(String username, String email, List<Poll> createdPolls, List<Vote> votes) {
        this.username = username;
        this.email = email;
        this.createdPolls = createdPolls;
        this.votes = votes;
    }
    public String getUsername() {
        return username;
    }
    public String getEmail() {
        return email;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public List<Poll> getCreatedPolls() {
        return createdPolls;
    }
    public void setCreatedPolls(List<Poll> createdPolls) {
        this.createdPolls = createdPolls;
    }
    public List<Vote> getVotes() {
        return votes;
    }
    public void setVotes(List<Vote> votes) {
        this.votes = votes;
    }
}
