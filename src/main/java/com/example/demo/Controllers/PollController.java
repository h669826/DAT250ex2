package com.example.demo.Controllers;

import com.example.demo.Services.PollService;
import com.example.demo.model.Poll;
import com.example.demo.model.User;
import com.example.demo.model.Vote;
import com.example.demo.model.VoteOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    public void addVote(@PathVariable("pid") UUID pid, @RequestBody Map<String, Object> body) {
        UUID userId = UUID.fromString(String.valueOf(body.get("user")));
        UUID optionId = UUID.fromString(String.valueOf(body.get("voteOption")));

        java.time.Instant publishedAt = null;
        Object ts = body.get("publishedAt");
        if (ts != null) {
            publishedAt = java.time.Instant.parse(String.valueOf(ts));
        }

        com.example.demo.model.User user = new com.example.demo.model.User();
        user.setId(userId);

        com.example.demo.model.VoteOption opt = new com.example.demo.model.VoteOption();
        opt.setId(optionId);

        com.example.demo.model.Vote vote = new com.example.demo.model.Vote();
        vote.setUser(user);
        vote.setVoteOption(opt);
        if (publishedAt != null) vote.setPublishedAt(publishedAt);

        pollService.addVote(pid, vote);
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
