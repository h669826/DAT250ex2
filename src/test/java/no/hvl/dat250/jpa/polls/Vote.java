package no.hvl.dat250.jpa.polls;

import jakarta.persistence.*;

@Entity
@Table(name = "votes")
public class Vote {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "voted_by_id", nullable = false)
    private User votedBy;

    @ManyToOne(optional = false)
    @JoinColumn(name = "vote_option_id", nullable = false)
    private VoteOption votesOn;

    protected Vote() {}
    public Vote(User votedBy, VoteOption votesOn) {
        this.votedBy = votedBy;
        this.votesOn = votesOn;
    }

    public Long getId() { return id; }
    public User getVotedBy() { return votedBy; }
    public VoteOption getVotesOn() { return votesOn; }
}
