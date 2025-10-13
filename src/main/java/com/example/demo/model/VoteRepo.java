package com.example.demo.model;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VoteRepo extends JpaRepository<Vote, UUID> {

    interface OptionCount {
        Integer getPresentationOrder();
        Long getCnt();
    }

    @Query("""
        SELECT vo.presentationOrder as presentationOrder, COUNT(v.id) as cnt
        FROM Vote v
        JOIN v.voteOption vo
        WHERE vo.poll.id = :pollId
        GROUP BY vo.presentationOrder
        ORDER BY vo.presentationOrder
    """)
    List<OptionCount> countByPollGrouped(@Param("pollId") UUID pollId);

    List<Vote> findByVoteOption_Poll_Id(UUID pollId);
}
