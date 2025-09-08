package com.example.demo.Services;

import com.example.demo.DomainManager;
import com.example.demo.model.Poll;
import com.example.demo.model.User;
import com.example.demo.model.Vote;
import com.example.demo.model.VoteOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PollServiceImp implements PollService {

    private DomainManager domainManager;

    @Autowired
    public PollServiceImp(DomainManager domainManager) {
        this.domainManager = domainManager;
    }

    @Override
    public void addPoll(UUID uid, Poll poll) {
        domainManager.addPoll(uid, poll);
    }

    @Override
    public void deletePoll(UUID pid) {
        domainManager.deletePoll(pid);
    }

    @Override
    public List<Poll> getPolls(UUID uid) {
        return domainManager.getPolls(uid);
    }

    @Override
    public void addVote(UUID pid, Vote vote) {
        domainManager.addVote(pid, vote);
    }

    @Override
    public List<Vote> getVotes(UUID pid) {
        return domainManager.getVotes(pid);
    }

    @Override
    public void addOption(UUID pid, VoteOption option) {
        domainManager.addOption(pid, option);
    }

    @Override
    public void deleteOption(UUID pid, UUID oid) {
        domainManager.deleteOption(pid, oid);
    }
}
