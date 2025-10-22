package com.example.demo.Controllers;

import com.example.demo.Services.PollService;
import com.example.demo.Services.UserService;
import com.example.demo.dto.CreatePollDto;
import com.example.demo.dto.UpdatePollDto;
import com.example.demo.model.*;
import com.fasterxml.jackson.annotation.JsonAlias;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
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

    private final PollService pollService;
    private final UserService userService;

    @Autowired
    public PollController(PollService pollService, UserService userService) {
        this.pollService = pollService;
        this.userService = userService;
    }

    @PostMapping(
            path = "/{uid:[0-9a-fA-F\\-]{36}}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public Poll createForUser(@PathVariable UUID uid, @RequestBody CreatePollDto dto) {
        return pollService.createForUser(uid, dto);
    }

    /*@GetMapping("/api/v1/users/by-email/{email}")
    public ResponseEntity<User> getByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }*/

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

    @GetMapping(path = "/{uid:[0-9a-fA-F\\-]{36}}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Poll> getPolls(@PathVariable UUID uid) {
        return pollService.getPolls(uid);
    }


    @PostMapping(path = "/{pid}/votes", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addVote(@PathVariable("pid") UUID pid, @RequestBody VoteRequest body) {
        if (body == null || body.getOptionId() == null || body.getUser() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing user/option");
        }
        pollService.addVote(pid, body.getUser(), body.getOptionId(), body.getPublishedAt());
    }


    @GetMapping(path = "/{pid}/votes", produces = MediaType.APPLICATION_JSON_VALUE)
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
