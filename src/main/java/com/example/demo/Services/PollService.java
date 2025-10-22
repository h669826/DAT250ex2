package com.example.demo.Services;

import com.example.demo.dto.CreatePollDto;
import com.example.demo.dto.UpdatePollDto;
import com.example.demo.model.Poll;
import com.example.demo.model.User;
import com.example.demo.model.Vote;
import com.example.demo.model.VoteOption;
import org.springframework.lang.Nullable;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface PollService {
    void addPoll(UUID uid, Poll poll);
    void deletePoll(UUID pid);
    List<Poll> getPolls(UUID uid);
    void addVote(UUID pollId, UUID userId, UUID optionId, Instant publishedAt);
    List<Vote> getVotes(UUID pid);
    void addOption(UUID pid, VoteOption option);
    void deleteOption(UUID pid, UUID oid);
    Poll updatePoll(UUID pollId, UpdatePollDto dto);
    Poll createForUser(UUID uid, CreatePollDto dto);
    void addVoteFromBroker(UUID pollId, UUID optionId, @Nullable UUID userId, Instant publishedAt);
    Map<UUID, Long> getTallies(UUID pollId);
}
