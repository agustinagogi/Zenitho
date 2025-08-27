package com.zenitho.api.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Table(name = "cards")
@Data
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String content;

    private int position;

    private boolean archived = false;

    @ManyToOne
    @JoinColumn(name = "column_id", nullable = false)
    private BoardColumn boardColumn;

    @ManyToMany
    @JoinTable(
            name = "card_assignees",
            joinColumns = @JoinColumn(name = "card_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> assignees;
}
