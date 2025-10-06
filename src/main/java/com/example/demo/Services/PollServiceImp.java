package com.example.demo.Services;

import com.example.demo.DomainManager;
import com.example.demo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class PollServiceImp implements PollService {

    private final PollResultService pollResultService;
    private DomainManager domainManager;
    private VoteRepo voteRepo;

    @Autowired
    public PollServiceImp(DomainManager domainManager, PollResultService pollResultService) {
        this.domainManager = domainManager;
        this.pollResultService = pollResultService;
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
    @Transactional
    public boolean addVote(UUID pollID, Vote vote) {
        voteRepo.save(vote);
        pollResultService.onVotePersisted(vote);
        return true;
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
