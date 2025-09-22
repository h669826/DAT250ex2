package no.hvl.dat250.jpa.polls;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "polls")
public class Poll {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String question;

    @ManyToOne(optional = false)
    @JoinColumn(name = "created_by_id", nullable = false)
    private User createdBy;

    @OneToMany(mappedBy = "poll", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("presentationOrder ASC")
    private List<VoteOption> options = new ArrayList<>();

    protected Poll() {}
    public Poll(String question, User createdBy) {
        this.question = question;
        this.createdBy = createdBy;
    }

    public VoteOption addVoteOption(String caption) {
        int order = options.size();
        VoteOption opt = new VoteOption(caption, order, this);
        options.add(opt);
        return opt;
    }

    public Long getId() { return id; }
    public String getQuestion() { return question; }
    public User getCreatedBy() { return createdBy; }
    public List<VoteOption> getOptions() { return options; }
}
