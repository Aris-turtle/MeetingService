package com.aristurtle.megakalservice.dto.util;

import com.aristurtle.megakalservice.dto.VoteDTO;
import com.aristurtle.megakalservice.dto.VotingDTO;
import com.aristurtle.megakalservice.dto.VotingDTOGetById;
import com.aristurtle.megakalservice.model.Voting;
import org.modelmapper.ModelMapper;

import java.util.Calendar;
import java.util.List;

public class VotingConverter {
    private static final ModelMapper modelMapper = new ModelMapper();

    public static Voting convertToVoting(VotingDTO votingDTO) {
        return modelMapper.map(votingDTO, Voting.class);
    }

    public static VotingDTO convertToVotingDTO(Voting voting) {
        return modelMapper.map(voting, VotingDTO.class);
    }

    public static VotingDTOGetById convertToVotingDTOForGETById(Voting voting) {
        final String title = voting.getTitle();
        final String creatorTgUsername = voting.getCreatorTgUsername();
        final List<VoteDTO> voteDTOList = VoteConverter.convertToVoteDTOList(voting.getVotes());
        final List<Calendar> dateToChoose = voting.getDateToChoose();
        return VotingDTOGetById.builder()
                .title(title)
                .creatorTgUsername(creatorTgUsername)
                .votes(voteDTOList)
                .dateToChoose(dateToChoose)
                .build();
    }
}
