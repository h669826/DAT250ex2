package com.example.demo.Controllers;

import com.example.demo.Services.PollService;
import com.example.demo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@CrossOrigin(
        origins = {
                "http://localhost:5173",
                "http://127.0.0.1:5173"
        },
        allowedHeaders = "*",
        methods = { GET, POST, PUT, PATCH, DELETE, OPTIONS },
        maxAge = 3600
)
@RestController
@RequestMapping("/api/v1/polls")
public class PollController {

    private VoteOptionRepo voteOptionRepo;
    private PollService pollService;

    @Autowired
    public PollController(VoteOptionRepo voteOptionRepo, PollService pollService) {
        this.voteOptionRepo = voteOptionRepo;
        this.pollService = pollService;
    }
    static class VoteRequest {
        private UUID optionId;     // test may send "optionId"
        private UUID voteOptionId; // or it may send "voteOptionId"

        public UUID getOptionId() { return optionId; }
        public void setOptionId(UUID optionId) { this.optionId = optionId; }
        public UUID getVoteOptionId() { return voteOptionId; }
        public void setVoteOptionId(UUID voteOptionId) { this.voteOptionId = voteOptionId; }

        UUID resolvedOptionId() {
            return optionId != null ? optionId : voteOptionId;
        }
    }

    @PostMapping("/{uid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addPoll(@PathVariable("uid") UUID uid, @RequestBody Poll poll) {
        pollService.addPoll(uid, poll);
    }
    @GetMapping("/{uid}")
    public List<Poll> getPolls(@PathVariable("uid") UUID uid) {
        return pollService.getPolls(uid);
    }


    @PostMapping("/{pid}/votes")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public boolean addVote(@PathVariable("pid") UUID pollId, @RequestBody VoteRequest body) {
        /*
        UUID userId = UUID.fromString(String.valueOf(body.get("user")));
        UUID optionId = UUID.fromString(String.valueOf(body.get("voteOption")));
        VoteOption option = voteOptionRepo.findById(optionId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Option not found"));

        java.time.Instant publishedAt = null;
        Object ts = body.get("publishedAt");
        if (ts != null) {
            publishedAt = java.time.Instant.parse(String.valueOf(ts));
        }

        User user = new User();
        user.setId(userId);

        option.setId(optionId);

        Vote vote = new Vote();
        vote.setUser(user);
        vote.setVoteOption(option);
        if (publishedAt != null) vote.setPublishedAt(publishedAt);

        pollService.addVote(pid, vote);*/
        UUID optionId = (body != null) ? body.resolvedOptionId() : null;
        if (optionId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing optionId/voteOptionId");
        }

        VoteOption option = voteOptionRepo.findById(optionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Option not found"));

        // Safety: ensure the option belongs to the URL poll
        if (option.getPoll() == null || !option.getPoll().getId().equals(pollId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Option does not belong to poll");
        }

        // Build the domain Vote and attach the managed option
        Vote vote = new Vote();
        vote.setVoteOption(option);

        return pollService.addVote(pollId, vote);
    }
    }

    @GetMapping("/{pid}/votes")
    public List<Vote> getVotes(@PathVariable("pid") UUID pid) {
        return pollService.getVotes(pid);
    }
    @DeleteMapping("/{pid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePoll(@PathVariable("pid") UUID pid) {
        pollService.deletePoll(pid);
    }
    @PostMapping("/{pid}/options")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addOption(@PathVariable("pid") UUID pid, @RequestBody VoteOption option) {
        pollService.addOption(pid, option);
    }
    @DeleteMapping("/{pid}/options/{oid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOption(@PathVariable("pid") UUID pid, @PathVariable("oid") UUID oid) {
        pollService.deleteOption(pid, oid);
    }

}
