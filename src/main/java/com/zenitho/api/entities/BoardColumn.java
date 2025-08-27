package com.zenitho.api.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name ="board_columns")
@Data
public class BoardColumn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private double position;

    // Relaciones
    @ManyToOne // Muchas columnas pueden pertenecer a un solo tablero
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    // Una columna puede tener muchas tarjetas
    // CascadeType.ALL si borramos una columna, todas las tarjetas que tiene dentro se borrarán automáticamente
    @OneToMany(mappedBy = "column", cascade = CascadeType.ALL)
    private List<Card> cards;
}
