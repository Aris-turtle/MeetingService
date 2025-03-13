package com.aristurtle.megakalservice.controller;

import com.aristurtle.megakalservice.dto.VotingDTO;
import com.aristurtle.megakalservice.dto.VotingDTOGetById;
import com.aristurtle.megakalservice.dto.util.Views;
import com.aristurtle.megakalservice.dto.util.VotingConverter;
import com.aristurtle.megakalservice.exception.InvalidVotingException;
import com.aristurtle.megakalservice.exception.VotingNotFoundException;
import com.aristurtle.megakalservice.model.Voting;
import com.aristurtle.megakalservice.service.VotingService;
import com.aristurtle.megakalservice.util.VotingErrorResponse;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/votings")
public class VotingController {
    private final VotingService votingService;
    private final ModelMapper modelMapper;

    @Autowired
    public VotingController(VotingService votingService, ModelMapper modelMapper) {
        this.votingService = votingService;
        this.modelMapper = modelMapper;
    }

    @PostMapping()
    @JsonView(Views.CreateVoting.class)
    public ResponseEntity<Long> createVoting(@RequestBody @Valid VotingDTO votingDTO,
                                             BindingResult bindingResult) {
        examineException(bindingResult);
        final Voting voting = VotingConverter.convertToVoting(votingDTO);
        final long id = votingService.save(voting);
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @GetMapping()
    @JsonView(Views.GetByTgUsername.class)
    public ResponseEntity<List<VotingDTO>> getVotingsByCreatorTgUsername(@RequestParam String creatorTgUsername) {
        List<Voting> votings = votingService.getVotings(creatorTgUsername);
        if (votings.isEmpty())
            throw new VotingNotFoundException("Does not found Votings for user=" + creatorTgUsername);
        final List<VotingDTO> responseList = votings.stream().map(VotingConverter::convertToVotingDTO).collect(Collectors.toList());
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VotingDTOGetById> getVotingById(@PathVariable("id") long id) {
        Optional<Voting> voting = votingService.getVoting(id);
        if (voting.isEmpty())
            throw new VotingNotFoundException("Does not found Voting with id=" + id);
        final VotingDTOGetById votingDTO = VotingConverter.convertToVotingDTOForGETById(voting.get());
        return ResponseEntity.ok(votingDTO);
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
    private ResponseEntity<VotingErrorResponse> handleException(InvalidVotingException e) {
        final VotingErrorResponse votingErrorResponse = new VotingErrorResponse(e.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(votingErrorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<VotingErrorResponse> handleException(VotingNotFoundException e) {
        final VotingErrorResponse votingErrorResponse = new VotingErrorResponse(e.getLocalizedMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(votingErrorResponse, HttpStatus.NOT_FOUND);
    }

}
