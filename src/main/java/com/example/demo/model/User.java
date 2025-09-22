package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class User {
    private UUID id = UUID.randomUUID();
    private String username;
    private String email;
    private List<Poll> createdPolls;
    private List<Vote> votes;

    public User(){}

    public User(String username, String email) {
        this.username = username;
        this.email = email;
        this.createdPolls = new ArrayList<>();
        this.votes = new ArrayList<>();
    }
    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
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

/*

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity
@Table(name = "users")
@Access(AccessType.PROPERTY)
public class User {

    private UUID id = UUID.randomUUID();
    private String username;
    private String email;
    private List<Poll> createdPolls = new ArrayList<>();
    private List<Vote> votes = new ArrayList<>();

    public User() {}
    public User(String username, String email) { this.username = username; this.email = email; }

    @Id
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    @Column(nullable = false, unique = true)
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    @Column(nullable = false, unique = true)
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Poll> getCreated() {
        if (createdPolls == null) createdPolls = new ArrayList<>();
        return createdPolls;
    }
    public void setCreated(List<Poll> created) { this.createdPolls = created; }

    @OneToMany(mappedBy = "votedBy", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Vote> getVotes() { return votes; }
    public void setVotes(List<Vote> votes) { this.votes = votes; }

    public Poll createPoll(String question) {
        Poll p = new Poll();
        p.setQuestion(question);
        p.setCreatedBy(this);
        createdPolls.add(p);
        return p;
    }

    public Vote voteFor(VoteOption option) {
        Vote v = new Vote();
        v.setVotedBy(this);
        v.setVotesOn(option);
        votes.add(v);
        return v;
    }

    public List<Poll> getCreatedPolls() { return createdPolls; }
    public void setCreatedPolls(List<Poll> cps) { this.createdPolls = cps; }

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

