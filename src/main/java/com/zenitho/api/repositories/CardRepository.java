package com.zenitho.api.repositories;

import com.zenitho.api.entities.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // Se conectará a la BBDD
public interface CardRepository extends JpaRepository<Card, Long> {
}
