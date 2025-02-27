package com.aristurtle.megakalservice.controller;

import com.aristurtle.megakalservice.dto.VoteDTO;
import com.aristurtle.megakalservice.exception.InvalidVoteException;
import com.aristurtle.megakalservice.exception.InvalidVotingException;
import com.aristurtle.megakalservice.exception.VoteNotFoundException;
import com.aristurtle.megakalservice.model.Vote;
import com.aristurtle.megakalservice.service.VoteService;
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

@RestController
@RequestMapping("/api/v1")
public class VoteController {
    private final VoteService voteService;
    private final ModelMapper modelMapper;

    @Autowired
    public VoteController(VoteService voteService, ModelMapper modelMapper) {
        this.voteService = voteService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/votings/{id}/votes")
    ResponseEntity<List<Long>> addVote(@PathVariable("id") long votingId, @RequestBody @Valid VoteDTO voteDTO,
                                       BindingResult bindingResult) {
        examineException(bindingResult);
        final List<Vote> votes = convertToVote(voteDTO);
        List<Long> ids = voteService.saveAll(votes, votingId);
        return new ResponseEntity<>(ids, HttpStatus.CREATED);
    }

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
