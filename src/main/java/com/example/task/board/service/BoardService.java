package com.example.task.board.service;

import com.example.task.board.dto.BoardDtoRes;
import com.example.task.board.dto.BoardDtoReq;
import com.example.task.board.model.BoardEntity;
import com.example.task.board.model.LikesEntity;
import com.example.task.board.repository.BoardRepository;
import com.example.task.user.model.UserEntity;
import com.example.task.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;


//    보드(게시판) 목록 가저오기
    public List<BoardDtoRes> boardList() {
        List<BoardDtoRes> boardDtoResList = new ArrayList<>();
        List<BoardEntity> boardEntitiyList = boardRepository.findAll();

        for(BoardEntity boardEntityObj : boardEntitiyList){
            boardDtoResList.add(BoardDtoRes.BoardDtoRes(boardEntityObj));
        }

        return boardDtoResList;
    }

    @Transactional
    public BoardDtoRes boardFindByIdAndUserEntityEmail(Long board_id, String email) {


        userRepository.findByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException("해당하는 이메일이 없습니다."));

//        boardRepository.findById(board_id); //board_id로 조회
//        Optional<BoardEntity> board = boardRepository.findByIdAndUserEntityEmail(board_id, email);
//        return board.orElseThrow(()-> new IllegalArgumentException("게시글이 삭제되었습니다."));

//        return boardRepository.findByIdAndUserEntityEmail(board_id, email)
//                .orElseThrow(()-> new IllegalArgumentException("게시글이 삭제되었습니다."));

        BoardEntity boardEntity = boardRepository.findByIdAndUserEntityEmail(board_id, email)
                    .orElseThrow(()-> new IllegalArgumentException("게시글이 삭제되었습니다."));

        BoardDtoRes boardDtoRes = BoardDtoRes.BoardDtoRes(boardEntity);
        return boardDtoRes;
    }

    public List<BoardDtoRes> boardFindAllByUserEntityEmail(String email, String targetEmail) {

        userRepository.findByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException("해당하는 이메일이 없습니다."));


//        return boardRepository.findAllByUserEntityId(user_id); id로 검색

        // like 에 좋아요한 닉네임이 안뜸
//        return boardRepository.findAllByUserEntityEmail(targetEmail);

        List<BoardDtoRes> boardDtoResList = new ArrayList<>();
        List<BoardEntity> boardEntitiyList = boardRepository.findAllByUserEntityEmail(targetEmail);

        for(BoardEntity boardEntityObj : boardEntitiyList){
            boardDtoResList.add(BoardDtoRes.BoardDtoRes(boardEntityObj));
        }

        return boardDtoResList;
    }



    @Transactional
    public BoardDtoRes boardSave(BoardDtoReq boardDtoReq){
        String email = boardDtoReq.getEmail();
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(()-> new NullPointerException("해당 이메일이 존제하지 않습니다."));

        BoardEntity boardEntity = BoardEntity.builder()
                .userEntity(userEntity)
                .nickname(userEntity.getNickname())
                .content(boardDtoReq.getContent())
                .imgName(boardDtoReq.getImgName())
                .imgPath(boardDtoReq.getImgPath())
                .layout(boardDtoReq.getLayout())
                .build();
        boardRepository.save(boardEntity);

        BoardDtoRes boardDtoRes = BoardDtoRes.BoardDtoRes(boardEntity);

        // boardRepository.save()->boardRepository.findAll() 순서로 하면 이상하게 안된다.
//        List<BoardDtoRes> boardDtoResList = new ArrayList<>();
//        List<BoardEntity> boardEntitiyList = boardRepository.findAll();
//
//        for(BoardEntity boardEntityObj : boardEntitiyList){
//            boardDtoResList.add(BoardDtoRes.BoardDtoRes(boardEntityObj));
//        }
        return boardDtoRes;
    }

    public BoardDtoRes boardUpdate(BoardDtoReq boardDtoReq, Long board_id) {
        BoardEntity boardEntity = boardRepository.findById(board_id).orElseThrow(()-> new NullPointerException("게시물이 없습니다."));

        String user_email = boardDtoReq.getEmail();
        UserEntity userEntity = userRepository.findByEmail(user_email).orElseThrow(()-> new NullPointerException("해당하는 이메일이 존재하지 않습니다."));


        boardEntity.update(boardDtoReq, userEntity);
//        boardEntity = BoardEntity.builder()
//                .userEntity(userEntity)
//                .content(boardDtoReq.getContent())
//                .imgName(boardDtoReq.getImgName())
//                .imgPath(boardDtoReq.getImgPath())
//                .nickname(boardDtoReq.getNickname())
//                .build();

        boardEntity.setNickname(userEntity.getNickname());

        boardRepository.save(boardEntity);

        BoardDtoRes boardDtoRes = BoardDtoRes.BoardDtoRes(boardEntity);
        return boardDtoRes;
    }

    @Transactional
    public Long boardDeleteById(Long board_id, String email){
        userRepository.findByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException("해당하는 이메일이 없습니다."));

        boardRepository.deleteById(board_id);
        return board_id;
    }

    @Transactional
    public void boardDeleteAllByIdList(String email) {

        userRepository.findByEmail(email)
                .orElseThrow(()-> new NullPointerException("해당 이메일이 존제하지 않습니다."));

        boardRepository.deleteAllByUserEntityEmail(email);
    }
}
