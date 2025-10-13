package com.example.demo.Services;

import com.example.demo.DomainManager;
import com.example.demo.dto.CreatePollDto;
import com.example.demo.dto.UpdatePollDto;
import com.example.demo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PollServiceImp implements PollService {

    private final PollResultService pollResultService;
    private final VoteRepo voteRepo;
    private final UserRepo userRepo;
    private final PollRepo pollRepo;
    private final VoteOptionRepo voteOptionRepo;


    @Autowired
    public PollServiceImp(PollResultService pollResultService, VoteRepo voteRepo, UserRepo userRepo, PollRepo pollRepo, VoteOptionRepo voteOptionRepo) {
        this.pollResultService = pollResultService;
        this.voteRepo = voteRepo;
        this.userRepo = userRepo;
        this.pollRepo = pollRepo;
        this.voteOptionRepo = voteOptionRepo;
    }

    @Override
    @Transactional
    public Poll createPoll(UUID userId, CreatePollDto dto) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Poll poll = new Poll();
        poll.setUser(user);
        poll.setCreatedBy(user);
        poll.setQuestion(dto.question());

        if (poll.getVoteOptions() == null) {
            poll.setVoteOptions(new ArrayList<>());
        }
        if (dto.options() != null) {
            for (String text : dto.options()) {
                VoteOption opt = new VoteOption();
                opt.setCaption(text);
                opt.setPoll(poll);
                poll.getVoteOptions().add(opt);
            }
        }
        return pollRepo.save(poll);
    }

    @Override
    @Transactional
    public Poll updatePoll(UUID pollId, UpdatePollDto dto) {
        Poll managed = pollRepo.findById(pollId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Poll not found"));

        if (dto.question() != null) {
            managed.setQuestion(dto.question());
        }

        if (dto.options() != null) {
            var existing = managed.getVoteOptions();

            dto.options().stream()
                    .filter(p -> p.remove() != null && p.remove() && p.id() != null)
                    .forEach(p -> existing.removeIf(opt -> opt.getId().equals(p.id())));

            dto.options().stream()
                    .filter(p -> p.id() != null && (p.remove() == null || !p.remove()))
                    .forEach(p -> existing.stream()
                            .filter(opt -> opt.getId().equals(p.id()))
                            .findFirst()
                            .ifPresent(opt -> opt.setCaption(p.text()))
                    );

            dto.options().stream()
                    .filter(p -> p.id() == null && (p.remove() == null || !p.remove()))
                    .forEach(p -> {
                        var opt = new com.example.demo.model.VoteOption();
                        opt.setCaption(p.text());
                        opt.setPoll(managed);
                        existing.add(opt);
                    });
        }

        return managed;
    }

    @Override
    @Transactional
    public void addPoll(UUID uid, Poll poll) {
        var userRef = userRepo.findById(uid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        poll.setUser(userRef);
        if (poll.getCreatedBy() == null) {
            poll.setCreatedBy(userRef);
        }

        if (poll.getVoteOptions() != null) {
            for (var opt : poll.getVoteOptions()) {
                opt.setPoll(poll);
            }
        }

        pollRepo.save(poll);
    }

    @Override
    @Transactional
    public boolean addVote(UUID pollID, Vote vote) {
        UUID userId   = vote.getUser() != null ? vote.getUser().getId() : null;
        UUID optionId = vote.getVoteOption() != null ? vote.getVoteOption().getId() : null;
        if (userId == null || optionId == null) return false;

        var userRef   = userRepo.getReferenceById(userId);
        var optionRef = voteOptionRepo.getReferenceById(optionId);

        var poll = optionRef.getPoll();
        if (poll == null || !pollID.equals(poll.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Option does not belong to poll");
        }

        vote.setUser(userRef);
        vote.setVoteOption(optionRef);
        if (vote.getPublishedAt() == null) {
            vote.setPublishedAt(Instant.now());
        }

        Vote saved = voteRepo.save(vote);
        pollResultService.onVotePersisted(saved); // if you maintain cache
        return true;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Vote> getVotes(UUID pid) {
        return voteRepo.findByVoteOption_Poll_Id(pid);
    }

    @Transactional
    @Override
    public void addOption(UUID pid, VoteOption option) {
        Poll poll = pollRepo.getReferenceById(pid);
        option.setPoll(poll);
        voteOptionRepo.save(option);
    }

    @Transactional
    @Override
    public void deleteOption(UUID pid, UUID oid) {
        VoteOption opt = voteOptionRepo.findById(oid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Option not found"));

        if (opt.getPoll() == null || !pid.equals(opt.getPoll().getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Option does not belong to poll");
        }

        voteOptionRepo.delete(opt);
    }

    @Override
    public void deletePoll(UUID pid) {
        Poll poll = pollRepo.getReferenceById(pid);
        pollRepo.delete(poll);
    }

    @Override
    public List<Poll> getPolls(UUID uid) {
        return pollRepo.findByUserId(uid);
    }
}
