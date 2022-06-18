package com.example.task.board.repository;

import com.example.task.board.model.BoardEntity;
import com.example.task.user.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {

    List<BoardEntity> findAllById(Long id);

    //user_id 의 모든 게시물
    List<BoardEntity> findAllByUserEntityId(Long id);

    Optional<BoardEntity> findByIdAndUserEntityEmail(Long board_id, String email);

    //board 해당유저의 이메일로 모든 게시물출력
    List<BoardEntity> findAllByUserEntityEmail(String email);

    //board 해당유저(자기자신)의 이메일로 모든 게시물 삭제
    void deleteAllByUserEntityEmail(String email);
}
