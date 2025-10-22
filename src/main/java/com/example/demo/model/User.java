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
@Access(AccessType.FIELD)
@Entity
@Table(name = "users")
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Poll> createdPolls = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vote> votes = new ArrayList<>();

    public User() {}
    public User(String username, String email) { this.username = username; this.email = email; }

    @Transient
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    @Transient
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    @Transient
    @JsonProperty("username")
    public String getName() { return username; }

    @JsonProperty("username")
    public void setName(String username) { this.username = username; }

    @Transient
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Transient
    @JsonProperty("created")
    public List<Poll> getCreated() {
        if (createdPolls == null) createdPolls = new ArrayList<>();
        return createdPolls;
    }
    @Transient
    public void setCreated(List<Poll> created) { this.createdPolls = created; }

    @Transient
    public List<Vote> getVotes() { return votes; }
    @Transient
    public void setVotes(List<Vote> votes) { this.votes = votes; }

    public Poll createPoll(String question) {
        Poll p = new Poll();
        p.setQuestion(question);
        p.setUser(this);
        createdPolls.add(p);
        return p;
    }

    public Vote voteFor(VoteOption option) {
        Vote v = new Vote();
        v.setUser(this);
        v.setVoteOption(option);
        votes.add(v);
        return v;
    }

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


