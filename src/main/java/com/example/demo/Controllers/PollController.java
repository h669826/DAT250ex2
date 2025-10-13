package com.example.demo.Controllers;

import com.example.demo.Services.PollService;
import com.example.demo.dto.CreatePollDto;
import com.example.demo.dto.UpdatePollDto;
import com.example.demo.model.*;
import com.fasterxml.jackson.annotation.JsonAlias;
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

    private PollService pollService;

    @Autowired
    public PollController(PollService pollService) {
        this.pollService = pollService;
    }

    @PostMapping("/{uid}")
    public Poll create(@PathVariable UUID uid, @RequestBody CreatePollDto dto) {
        return pollService.createPoll(uid, dto);
    }

    @PutMapping("/{pollId}")
    public Poll update(@PathVariable UUID pollId, @RequestBody UpdatePollDto dto) {
        return pollService.updatePoll(pollId, dto);
    }

    static class VoteRequest {
        @JsonAlias({"voteOption", "voteOptionId"})
        private UUID optionId;

        private UUID user;

        private java.time.Instant publishedAt;

        public UUID getOptionId() { return optionId; }
        public void setOptionId(UUID optionId) { this.optionId = optionId; }

        public UUID getUser() { return user; }
        public void setUser(UUID user) { this.user = user; }

        public java.time.Instant getPublishedAt() { return publishedAt; }
        public void setPublishedAt(java.time.Instant publishedAt) { this.publishedAt = publishedAt; }
    }

    @GetMapping("/{uid}")
    public List<Poll> getPolls(@PathVariable("uid") UUID uid) {
        return pollService.getPolls(uid);
    }


    @PostMapping("/{pid}/votes")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addVote(@PathVariable("pid") UUID pid, @RequestBody VoteRequest body) {
        if (body == null || body.getOptionId() == null || body.getUser() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing user/option");
        }
        var v = new Vote();

        var vo = new VoteOption();
        vo.setId(body.getOptionId());
        v.setVoteOption(vo);

        var u = new User();
        u.setId(body.getUser());
        v.setUser(u);

        v.setPublishedAt(body.getPublishedAt());

        boolean ok = pollService.addVote(pid, v);
        if (!ok) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vote was rejected");
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
