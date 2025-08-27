package com.zenitho.api.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "focus_sessions")
@Data
public class FocusSession {

    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime startTime;

    private LocalDateTime endTime;

    // Relaciones

    @ManyToOne // Muchas sesiones pueden pertenecer a un solo usuario
    @JoinColumn (name = "user_id", nullable = false)
    private User user;

    @ManyToOne // Muchas sesiones pertencen a una sola tarjeta
    @JoinColumn(name = "card_ id", nullable = false)
    private Card card;

}
