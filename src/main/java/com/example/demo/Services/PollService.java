package com.example.demo.Services;

import com.example.demo.model.Poll;
import com.example.demo.model.User;
import com.example.demo.model.Vote;
import com.example.demo.model.VoteOption;

import java.util.List;
import java.util.UUID;

public interface PollService {
    void addPoll(UUID uid, Poll poll);
    void deletePoll(UUID pid);
    List<Poll> getPolls(UUID uid);
    void addVote(UUID pid, Vote vote);
    List<Vote> getVotes(UUID pid);
    void addOption(UUID pid, VoteOption option);
    void deleteOption(UUID pid, UUID oid);
}
