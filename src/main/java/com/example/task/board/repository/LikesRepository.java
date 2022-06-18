package com.example.task.board.repository;

import com.example.task.board.model.BoardEntity;
import com.example.task.board.model.LikesEntity;
import com.example.task.user.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikesRepository extends JpaRepository<LikesEntity, Long> {
    // LikeEntity -> userEntityId -> id
    // LikeEntity -> boardEntityId -> id
    Optional<LikesEntity> findByUserEntityIdIdAndBoardEntityIdId(Long user_id, Long board_id);

    void deleteByUserEntityIdIdAndBoardEntityIdId(Long user_id, Long board_id);
}
