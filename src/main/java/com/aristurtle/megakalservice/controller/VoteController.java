package com.aristurtle.megakalservice.controller;

import com.aristurtle.megakalservice.dto.VoteDTO;
import com.aristurtle.megakalservice.exception.InvalidVoteException;
import com.aristurtle.megakalservice.exception.InvalidVotingException;
import com.aristurtle.megakalservice.exception.VoteNotFoundException;
import com.aristurtle.megakalservice.exception.VotingNotFoundException;
import com.aristurtle.megakalservice.model.Vote;
import com.aristurtle.megakalservice.service.VoteService;
import com.aristurtle.megakalservice.service.VotingService;
import com.aristurtle.megakalservice.util.VoteErrorResponse;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.aristurtle.megakalservice.dto.util.VoteConverter.convertToVote;
import static com.aristurtle.megakalservice.dto.util.VoteConverter.convertToVoteDTOList;

@RestController
@RequestMapping("/api/v1")
public class VoteController {
    private final VoteService voteService;
    private final VotingService votingService;

    @Autowired
    public VoteController(VoteService voteService, VotingService votingService) {
        this.voteService = voteService;
        this.votingService = votingService;
    }

    @PostMapping("/votings/{voting_id}/votes")
    public ResponseEntity<List<Long>> addVote(@PathVariable("voting_id") long votingId, @RequestBody @Valid VoteDTO voteDTO,
                                              BindingResult bindingResult) {
        examineException(bindingResult);
        final List<Vote> votes = convertToVote(voteDTO);
        List<Long> ids = voteService.saveAll(votes, votingId);
        return new ResponseEntity<>(ids, HttpStatus.CREATED);
    }

    @GetMapping("/votings/{voting_id}/votes")
    public ResponseEntity<List<VoteDTO>> getVotes(@PathVariable("voting_id") long votingId) {
        if (votingService.getVoting(votingId).isEmpty())
            throw new VotingNotFoundException("Does not found Voting with id=" + votingId);
        final List<Vote> votes = voteService.findByVotingId(votingId);
        return ResponseEntity.ok(convertToVoteDTOList(votes));
    }

    @GetMapping("/votings/{voting_id}/votes/{voter_tg_username}")
    public ResponseEntity<List<Long>> getVotes(@PathVariable("voting_id") long votingId,
                                               @PathVariable("voter_tg_username") String voterTgUsername) {
        final List<Vote> votes = voteService.findByVotingIdAndVoterTgUsername(votingId, voterTgUsername);
        if (votes.isEmpty())
            throw new VoteNotFoundException("Does not found Votes with parameters: voting_id=" + votingId + ", voter_tg_username=" + voterTgUsername);
        final List<Long> marks = votes.stream().map(v -> v.getMarkedDate().getTimeInMillis()).toList();
        return ResponseEntity.ok(marks);
    }

//    @PutMapping("/votings/{voting_id}/votes/{voter_tg_username}")
//    public ResponseEntity<List<Long>> updateVote(@PathVariable("voting_id") long votingId,
//                                                 @PathVariable("voter_tg_username") String voterTgUsername,
//                                                 @RequestBody List<Long> newMarks) {
//        final List<Vote> votes = voteService.findByVotingIdAndVoterTgUsername(votingId, voterTgUsername);
//        if (votes.isEmpty())
//            throw new VoteNotFoundException("Does not found Votes with parameters: voting_id=" + votingId + ", voter_tg_username=" + voterTgUsername);
//        List<Long> updatedMarksIds = voteService.updateMarks(votingId, voterTgUsername, newMarks);
//        return ngew ResponseEntity<>(updatedMarksIds, HttpStatus.OK);
//    }

    private void examineException(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            final StringBuilder errorMessage = new StringBuilder();
            final List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError error : fieldErrors)
                errorMessage.append(error).append(";");
            throw new InvalidVotingException(errorMessage.toString());
        }
    }

    @ExceptionHandler
    private ResponseEntity<VoteErrorResponse> handleException(InvalidVoteException e) {
        final VoteErrorResponse voteErrorResponse = new VoteErrorResponse(e.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(voteErrorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<VoteErrorResponse> handleException(VoteNotFoundException e) {
        final VoteErrorResponse voteErrorResponse = new VoteErrorResponse(e.getLocalizedMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(voteErrorResponse, HttpStatus.NOT_FOUND);
    }

}
