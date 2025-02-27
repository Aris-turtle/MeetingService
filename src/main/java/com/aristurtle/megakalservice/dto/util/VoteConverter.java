package com.aristurtle.megakalservice.dto.util;

import com.aristurtle.megakalservice.dto.VoteDTO;
import com.aristurtle.megakalservice.model.Vote;

import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

public class VoteConverter {
    public static List<Vote> convertToVote(VoteDTO voteDTO) {
        return voteDTO.getMarks().stream()
                .map(mark -> {
                    Vote vote = new Vote();
                    vote.setVoterTgUsername(voteDTO.getVoterTgUsername());

                    // Преобразуем long в Calendar
                    Calendar markedDate = Calendar.getInstance();
                    markedDate.setTimeInMillis(mark);
                    vote.setMarkedDate(markedDate);

                    return vote;
                })
                .collect(Collectors.toList());
    }

    public static VoteDTO convertToVoteDTO(List<Vote> votes) {
        if (votes == null || votes.isEmpty()) {
            return new VoteDTO();
        }

        VoteDTO voteDTO = new VoteDTO();
        voteDTO.setVoterTgUsername(votes.getFirst().getVoterTgUsername()); // Берем имя пользователя из первого Vote

        // Преобразуем markedDate из каждого Vote в long и добавляем в marks
        List<Long> marks = votes.stream()
                .map(vote -> vote.getMarkedDate().getTimeInMillis())
                .collect(Collectors.toList());

        voteDTO.setMarks(marks);
        return voteDTO;
    }
}
