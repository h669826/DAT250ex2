package com.example.demo.Services;

import com.example.demo.dto.CreatePollDto;
import com.example.demo.dto.UpdatePollDto;
import com.example.demo.messaging.PollMessaging;
import com.example.demo.model.*;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PollServiceImp implements PollService {

    private final PollResultService pollResultService;
    private final VoteRepo voteRepo;
    private final UserRepo userRepo;
    private final PollRepo pollRepo;
    private final VoteOptionRepo voteOptionRepo;
    @Value("${messaging.enabled:false}")
    boolean messagingEnabled;
    @Autowired(required=false)
    PollMessaging pollMessaging;
    private final AmqpAdmin amqp;
    private final TopicExchange pollsExchange;


    @Autowired
    public PollServiceImp(PollResultService pollResultService, VoteRepo voteRepo, UserRepo userRepo, PollRepo pollRepo, VoteOptionRepo voteOptionRepo, AmqpAdmin amqp, TopicExchange pollsExchange) {
        this.pollResultService = pollResultService;
        this.voteRepo = voteRepo;
        this.userRepo = userRepo;
        this.pollRepo = pollRepo;
        this.voteOptionRepo = voteOptionRepo;
        this.amqp = amqp;
        this.pollsExchange = pollsExchange;
    }

    @Transactional
    public Poll createPoll(CreatePollDto dto, UUID ownerUserId) {
        Poll poll = pollRepo.save(createForUser(ownerUserId, dto));

        String qName = "poll." + poll.getId() + ".votes";
        String rk    = "poll." + poll.getId() + ".votes";

        Queue q = new Queue(qName, false, false, true);
        amqp.declareQueue(q);

        Binding b = BindingBuilder.bind(q).to(pollsExchange).with(rk);
        amqp.declareBinding(b);

        return poll;
    }

    @Override
    @Transactional
    public Poll createForUser(UUID uid, CreatePollDto dto) {
        User owner = userRepo.findById(uid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No such user"));

        if (dto == null || dto.getQuestion() == null || dto.getQuestion().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing question");
        }

        Poll p = new Poll();
        p.setQuestion(dto.getQuestion());
        p.setUser(owner);

        if (dto.getPublishedAt() != null) {
            p.setPublishedAt(dto.getPublishedAt());
        } else {
            p.setPublishedAt(Instant.now());
        }

        if (dto.getValidUntil() != null) {
            p.setValidUntil(dto.getValidUntil());
        }

        if (dto.getVoteOptions() != null) {
            for (CreatePollDto.OptionDto o : dto.getVoteOptions()) {
                VoteOption vo = new VoteOption();
                vo.setCaption(o.getCaption());
                vo.setPresentationOrder(o.getPresentationOrder());
                p.addOption(vo);
            }
        }

        return pollRepo.save(p);
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

        poll.setCreator(userRef);
        if (poll.getUser() == null) {
            poll.setUser(userRef);
        }

        if (poll.getVoteOptions() != null) {
            for (var opt : poll.getVoteOptions()) {
                opt.setPoll(poll);
            }
        }

        pollRepo.save(poll);

        if (messagingEnabled && pollMessaging != null) {
            pollMessaging.registerPollTopic(poll.getId());
        }

    }

    @Transactional
    public void addVote(UUID pollId, UUID userId, UUID optionId, Instant publishedAt) {
        Poll poll = pollRepo.findById(pollId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Poll not found"));

        VoteOption option = voteOptionRepo.findById(optionId)
                .filter(vo -> vo.getPoll() != null && pollId.equals(vo.getPoll().getId()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Option not in this poll"));

        User user = null;
        if (userId != null) {
            user = userRepo.findById(userId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        }

        Vote v = new Vote();
        v.setPoll(poll);
        v.setVoteOption(option);
        v.setUser(user);
        v.setPublishedAt(publishedAt != null ? publishedAt : Instant.now());

        voteRepo.save(v);
    }

    @Transactional(readOnly = true)
    public List<Vote> getVotes(UUID pollId) {
        return voteRepo.findByVoteOption_Poll_IdOrderByPublishedAtAsc(pollId);
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
    @Transactional
    public void deletePoll(UUID pollId) {
        if (!pollRepo.existsById(pollId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Poll not found");
        }
        voteRepo.deleteByVoteOption_Poll_Id(pollId);

        voteOptionRepo.deleteByPoll_Id(pollId);

        pollRepo.deleteById(pollId);
    }

    @Override
    public List<Poll> getPolls(UUID uid) {
        return pollRepo.findByUserId(uid);
    }

    @Transactional
    @Override
    public void addVoteFromBroker(UUID pollId, UUID optionId, @Nullable UUID userId, Instant publishedAt) {
        var opt = voteOptionRepo.findById(optionId)
                .orElseThrow(() -> new IllegalArgumentException("Unknown option: " + optionId));

        Vote v = new Vote();
        v.setVoteOption(opt);
        v.setPublishedAt(publishedAt != null ? publishedAt : Instant.now());
        if (userId != null) v.setUser(userRepo.findById(userId).get());
        voteRepo.save(v);
    }
    @Transactional(readOnly = true)
    public Map<UUID, Long> getTallies(UUID pollId) {
        if (!pollRepo.existsById(pollId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Poll not found");
        }
        Map<UUID, Long> tallies = new HashMap<>();
        voteRepo.countByPollGrouped(pollId).forEach(r -> tallies.put(r.getOptionId(), r.getCnt()));
        pollRepo.findById(pollId)
                .ifPresent(p -> p.getVoteOptions()
                        .forEach(opt -> tallies.putIfAbsent(opt.getId(), 0L)));
        return tallies;
    }
}
