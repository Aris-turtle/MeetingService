package com.aristurtle.megakalservice.service;

import com.aristurtle.megakalservice.model.Voting;
import com.aristurtle.megakalservice.repository.VotingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class VotingService {
    private final VotingRepository votingRepository;

    public VotingService(VotingRepository votingRepository) {
        this.votingRepository = votingRepository;
    }

    @Transactional
    public long save(Voting voting) {
        return votingRepository.save(voting).getId();
    }

    public Optional<Voting> getVoting(long id) {
        return votingRepository.findById(id);
    }

    public List<Voting> getVotings(String creatorTgUsername) {
        return votingRepository.findAllByCreatorTgUsername(creatorTgUsername);
    }
}
