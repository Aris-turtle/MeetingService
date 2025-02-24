package com.aristurtle.megakalservice.repository;

import com.aristurtle.megakalservice.model.Voting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VotingRepository extends JpaRepository<Voting, Long> {
    List<Voting> findAllByCreatorTgUsername(String creatorTgUsername);
}
