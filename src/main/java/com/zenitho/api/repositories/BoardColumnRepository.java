package com.zenitho.api.repositories;

import com.zenitho.api.entities.BoardColumn;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardColumnRepository extends CrudRepository<BoardColumn, Long> {
}
