package com.example.task.board.service;

import com.example.task.board.dto.LikesDto;
import com.example.task.board.model.BoardEntity;
import com.example.task.board.model.LikesEntity;
import com.example.task.board.repository.BoardRepository;
import com.example.task.board.repository.LikesRepository;
import com.example.task.user.model.UserEntity;
import com.example.task.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class LikesService {

    private final LikesRepository likesRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @Transactional
    public String likesHit(String email, Long boardId) {

        BoardEntity boardEntity = boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException("해당하는 게시판이 존재하지 않습니다")
        );

        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("해당하는 아이디가 존재하지 않습니다")
        );

        LikesEntity likesEntity = LikesEntity.builder()
                .userEntityId(userEntity)
                .boardEntityId(boardEntity)
                .build();

        //좋아요 유무확인
        Long user_id = userEntity.getId();
        Long board_id = boardEntity.getId();
        Optional<LikesEntity> likeOptional = likesRepository.findByUserEntityIdIdAndBoardEntityIdId(user_id, board_id);

        if (likeOptional.isPresent()) {
            likesRepository.deleteByUserEntityIdIdAndBoardEntityIdId(user_id, board_id);
            return "좋아요 취소";
        } else {
            likesRepository.save(likesEntity);
            return "좋아요";
        }
    }

}
