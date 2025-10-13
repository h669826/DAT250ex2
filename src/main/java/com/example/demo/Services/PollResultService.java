package com.example.demo.Services;

import com.example.demo.model.Vote;
import com.example.demo.model.VoteOption;
import com.example.demo.model.VoteOptionRepo;
import com.example.demo.model.VoteRepo;
import com.example.demo.redis.Cache;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class PollResultService {

    private final VoteOptionRepo voteOptionRepo;
    private final VoteRepo voteRepo;
    private final Cache cache;

    public PollResultService(VoteOptionRepo voteOptionRepo, VoteRepo voteRepo, Cache cache) {
        this.voteOptionRepo = voteOptionRepo;
        this.voteRepo = voteRepo;
        this.cache = cache;
    }

    @Transactional(readOnly = true)
    public LinkedHashMap<Integer, Long> getCounts(UUID pollId) {
        Map<Integer, Long> cached = cache.get(pollId);
        if (cached != null) {return new LinkedHashMap<>(cached);}

        List<VoteOption> voteOptions = voteOptionRepo.findByPollIdOrderByPresentationOrderAsc(pollId);
        LinkedHashMap<Integer, Long> result = new LinkedHashMap<>();
        for (VoteOption voteOption : voteOptions) {result.put(voteOption.getPresentationOrder(), 0L);

        for (VoteRepo.OptionCount optionCount : voteRepo.countByPollGrouped(pollId)) {
            result.put(optionCount.getPresentationOrder(), optionCount.getCnt());}
        }

        cache.put(pollId, result, 120);

        return result;
    }

    @Transactional
    public void onVotePersisted(Vote vote){
        int ord = vote.getVoteOption().getPresentationOrder();
        UUID optionId = vote.getOption().getId();
        UUID pollId = voteOptionRepo.findPollIdByOptionId(optionId);
        cache.increment(pollId, ord);
    }
}
