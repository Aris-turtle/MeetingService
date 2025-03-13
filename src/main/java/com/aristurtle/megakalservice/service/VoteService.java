package com.aristurtle.megakalservice.service;

import com.aristurtle.megakalservice.exception.VotingNotFoundException;
import com.aristurtle.megakalservice.model.Vote;
import com.aristurtle.megakalservice.model.Voting;
import com.aristurtle.megakalservice.repository.VoteRepository;
import com.aristurtle.megakalservice.repository.VotingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class VoteService {
    private final VoteRepository voteRepository;
    private final VotingRepository votingRepository;

    @Autowired
    public VoteService(VoteRepository voteRepository, VotingRepository votingRepository) {
        this.voteRepository = voteRepository;
        this.votingRepository = votingRepository;
    }

    @Transactional
    public long save(Vote vote, long votingId) {
        final Voting voting = votingRepository.findById(votingId)
                .orElseThrow(() -> new VotingNotFoundException("Не найдено Голосование по id=" + votingId));
        vote.setVoting(voting);
        return voteRepository.save(vote).getId();
    }

    @Transactional
    public List<Long> saveAll(List<Vote> vote, long votingId) {
        final Voting voting = votingRepository.findById(votingId)
                .orElseThrow(() -> new VotingNotFoundException("Не найдено Голосование по id=" + votingId));
        vote.forEach(v -> v.setVoting(voting));
        final List<Vote> savedVotes = voteRepository.saveAll(vote);
        return savedVotes.stream().map(Vote::getId).toList();
    }

//    @Transactional
//    public List<Long> updateMarks(long votingId, String voterTgUsername, List<Long> newMarks) {
//        List<Vote> votes = findByVotingIdAndVoterTgUsername(votingId, voterTgUsername);
//        return voteRepository.save();
//    }

    public List<Vote> findByVotingId(long votingId) {
        return voteRepository.findByVotingId(votingId);
    }

    public List<Vote> findByVotingIdAndVoterTgUsername(long votingId, String voterTgUsername) {
        return voteRepository.findByVotingIdAndVoterTgUsername(votingId, voterTgUsername);
    }
}
