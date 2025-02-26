package com.aristurtle.megakalservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity(name = "votings")
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Voting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "creator_tg_username")
    private String creatorTgUsername;

    @Column(name = "title")
    private String title;

    @OneToMany(mappedBy = "voting", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vote> votes;
}
