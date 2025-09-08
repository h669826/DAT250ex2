package com.example.demo;

import com.example.demo.model.Poll;
import com.example.demo.model.User;
import com.example.demo.model.Vote;
import com.example.demo.model.VoteOption;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.*;

@Component
public class DomainManager {

    private final Map<UUID, User> users = new HashMap<>();
    private final Map<UUID, List<Poll>> userPolls = new HashMap<>();
    private final Map<UUID, List<Vote>> pollVotes = new HashMap<>();

    public void addUser(User user) {
        if (user.getId() == null) user.setId(UUID.randomUUID());
        users.put(user.getId(), user);
    }

    public User getUser(UUID uid) {
        return users.get(uid);
    }

    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    public void deleteUser(UUID uid) {
        users.remove(uid);
        userPolls.remove(uid);
        for (List<Vote> votes : pollVotes.values()) {
            votes.removeIf(v -> v.getUser() != null && uid.equals(v.getUser().getId()));
        }
    }

    public void addPoll(UUID uid, Poll poll) {
        if (poll.getId() == null) poll.setId(UUID.randomUUID());
        if (poll.getPublishedAt() == null) poll.setPublishedAt(Instant.now());


        User creator = users.getOrDefault(uid, new User());
        creator.setId(uid);
        poll.setCreator(creator);

        if (poll.getVoteOptions() == null) {
            poll.setVoteOptions(new ArrayList<>());
        }
        for (VoteOption opt : poll.getVoteOptions()) {
            if (opt.getId() == null) opt.setId(UUID.randomUUID());
            opt.setPoll(poll);
        }

        userPolls.computeIfAbsent(uid, k -> new ArrayList<>()).add(poll);
        pollVotes.putIfAbsent(poll.getId(), new ArrayList<>());
    }

    public List<Poll> getPolls(UUID uid) {
        return new ArrayList<>(userPolls.getOrDefault(uid, Collections.emptyList()));
    }

    public void deletePoll(UUID pid) {
        for (List<Poll> polls : userPolls.values()) {
            polls.removeIf(p -> pid.equals(p.getId()));
        }
        pollVotes.remove(pid);
    }

    public void addVote(UUID pid, Vote vote) {
        if (vote.getId() == null) vote.setId(UUID.randomUUID());
        if (vote.getPublishedAt() == null) vote.setPublishedAt(Instant.now());
        pollVotes.computeIfAbsent(pid, k -> new ArrayList<>()).add(vote);
    }

    public List<Vote> getVotes(UUID pid) {
        return new ArrayList<>(pollVotes.getOrDefault(pid, Collections.emptyList()));
    }

    public void addOption(UUID pid, VoteOption option) {
        Poll poll = findPollById(pid);
        if (poll == null) return;
        if (option.getId() == null) option.setId(UUID.randomUUID());
        option.setPoll(poll);
        if (option.getPresentationOrder() == 0) {
            int next = poll.getVoteOptions().stream()
                    .map(VoteOption::getPresentationOrder)
                    .max(Comparator.naturalOrder()).orElse(0) + 1;
            option.setPresentationOrder(next);
        }
        poll.getVoteOptions().add(option);
    }

    public void deleteOption(UUID pid, UUID oid) {
        Poll poll = findPollById(pid);
        if (poll == null) return;
        poll.getVoteOptions().removeIf(o -> oid.equals(o.getId()));
    }

    private Poll findPollById(UUID pid) {
        for (List<Poll> polls : userPolls.values()) {
            for (Poll p : polls) {
                if (pid.equals(p.getId())) return p;
            }
        }
        return null;
    }
}

