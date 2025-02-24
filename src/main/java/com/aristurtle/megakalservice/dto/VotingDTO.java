package com.aristurtle.megakalservice.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class VotingDTO {
    private String title;

    @NotEmpty
    private String creatorTgUsername;
}
