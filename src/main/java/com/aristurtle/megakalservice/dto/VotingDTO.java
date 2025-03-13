package com.aristurtle.megakalservice.dto;

import com.aristurtle.megakalservice.dto.util.Views;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.Calendar;
import java.util.List;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VotingDTO {
    @JsonView(Views.GetByTgUsername.class)
    private long id;

    @JsonView({Views.GetByTgUsername.class, Views.GetById.class, Views.CreateVoting.class})
    private String title;

    @NotEmpty
    @JsonView({Views.CreateVoting.class, Views.GetById.class})
    private String creatorTgUsername;

    private List<VoteDTO> votes;

    private List<Calendar> dateToChoose;
}
