package com.example.demo.model;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VoteRepo extends JpaRepository<Vote, UUID> {

    List<Vote> findByVoteOption_Poll_IdOrderByPublishedAtAsc(UUID pollId);

    void deleteByVoteOption_Poll_Id(UUID pollId);

    interface OptionCount {
        UUID getOptionId();
        Integer getPresentationOrder();
        Long getCnt();
    }

    record AggregateRow(UUID optionId, long count) {}


    @Query("""
    select  vo.id as optionId,
            vo.presentationOrder as presentationOrder,
            count(v) as cnt
    from Vote v
      join v.voteOption vo
    where vo.poll.id = :pollId
    group by vo.id, vo.presentationOrder
    order by vo.presentationOrder
  """)
    List<OptionCount> countByPollGrouped(@Param("pollId") UUID pollId);

    List<Vote> findByVoteOption_Poll_Id(UUID pollId);
}
