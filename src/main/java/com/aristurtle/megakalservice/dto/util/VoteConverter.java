package com.aristurtle.megakalservice.dto.util;

import com.aristurtle.megakalservice.dto.VoteDTO;
import com.aristurtle.megakalservice.model.Vote;

import java.util.*;
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

    public static List<VoteDTO> convertToVoteDTOList(List<Vote> votes) {
        Map<String, List<Long>> usernamePerMarks = new HashMap<>();
        for (Vote vote : votes) {
            final String voterTgUsername = vote.getVoterTgUsername();
            final long mark = vote.getMarkedDate().getTimeInMillis();
            if (usernamePerMarks.containsKey(voterTgUsername)) {
                usernamePerMarks.get(voterTgUsername).add(mark);
            } else {
                ArrayList<Long> marks = new ArrayList<>();
                marks.add(mark);
                usernamePerMarks.put(voterTgUsername, marks);
            }
        }

        return usernamePerMarks.entrySet().stream()
                .map(entry -> new VoteDTO(entry.getKey(), entry.getValue()))
                .toList();
    }
}
