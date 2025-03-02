package com.aristurtle.megakalservice.repository;

import com.aristurtle.megakalservice.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    List<Vote> findByVotingId(Long votingId);
    List<Vote> findByVotingIdAndVoterTgUsername(Long votingId, String voterTgUsername);
}
