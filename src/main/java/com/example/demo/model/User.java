package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)  // Hibernate 6 + Spring Boot 3.x
    @Column(nullable = false, updatable = false)
    private java.util.UUID id;
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

    @Transient
    @JsonProperty("username")
    public String getName() { return username; }

    @JsonProperty("username")
    public void setName(String username) { this.username = username; }

    @Column(nullable = false, unique = true)
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    public List<Poll> getCreated() {
        if (createdPolls == null) createdPolls = new ArrayList<>();
        return createdPolls;
    }
    public void setCreated(List<Poll> created) { this.createdPolls = created; }

    @OneToMany(mappedBy = "votedBy", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
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

    @Transient
    public List<Poll> getCreatedPolls() { return createdPolls; }
    @Transient
    public void setCreatedPolls(List<Poll> cps) { this.createdPolls = cps; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User other)) return false;
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

}


