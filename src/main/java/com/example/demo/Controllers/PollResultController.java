package com.example.demo.Controllers;

import com.example.demo.Services.PollResultService;
import com.example.demo.model.VoteOption;
import com.example.demo.model.VoteOptionRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class PollResultController {
    private final PollResultService pollResultService;
    private final VoteOptionRepo voteOptionRepo;

    public PollResultController(PollResultService pollResultService, VoteOptionRepo voteOptionRepo) {
        this.pollResultService = pollResultService;
        this.voteOptionRepo = voteOptionRepo;
    }

    @GetMapping("/polls/{pollId}/results")
    public ResponseEntity<List<Map<String,Object>>> getResults(@PathVariable UUID pollId) {
        LinkedHashMap<Integer, Long> results = pollResultService.getCounts(pollId);

        List<VoteOption> options = voteOptionRepo.findByPollIdOrderByPresentationOrderAsc(pollId);
        Map<Integer, String> captions = options.stream().collect(Collectors.toMap(VoteOption::getPresentationOrder, VoteOption::getCaption));

        List<Map<String,Object>> pollResults = new ArrayList<>();
        results.forEach((key, value) -> {
            Map<String,Object> pollResult = new LinkedHashMap<>();
            pollResult.put("presentationOrder", key);
            pollResult.put("caption", captions.getOrDefault(key, ""));
            pollResult.put("count", value);
            pollResults.add(pollResult);
        });
        return ResponseEntity.ok(pollResults);
    }
}
