package com.aristurtle.megakalservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Calendar;

@Entity(name = "votes")
@Setter
@Getter
@ToString
public class Vote {
    @Id
    @Column(name = "vote_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "voting_id", referencedColumnName = "id")
    private Voting voting;

    @Column(name = "voter_tg_username")
    private String voterTgUsername;

    @Column(name = "marked_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar markedDate;
}
