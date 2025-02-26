package com.aristurtle.megakalservice.dto;

import lombok.*;

import java.util.List;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class VoteDTO {
    private String voterTgUsername;

    private List<Long> marks;
}
