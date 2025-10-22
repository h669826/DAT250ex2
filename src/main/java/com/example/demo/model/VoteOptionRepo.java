package com.example.demo.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface VoteOptionRepo extends JpaRepository<VoteOption, UUID> {
    List<VoteOption> findByPollIdOrderByPresentationOrderAsc(UUID pollId);

    void deleteByPoll_Id(UUID pollId);


    @Query("select vo.poll.id from VoteOption vo where vo.id = :id")
    UUID findPollIdByOptionId(@Param("id") UUID id);
}
