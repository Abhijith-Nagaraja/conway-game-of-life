package com.gameoflife.repository;

import com.gameoflife.model.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Board entity.
 * Provides methods to interact with the board data in the database.
 */
@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    /**
     * Find boards with a specific generation count.
     * Useful for finding boards at specific iterations.
     */
    Iterable<Board> findByGeneration(int generation);
    
    /**
     * Find boards that have reached their final state.
     */
    Iterable<Board> findByFinalStateTrue();
}
