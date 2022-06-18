package com.example.task.board.controller;

import com.example.task.board.dto.BoardDtoRes;
import com.example.task.board.dto.BoardDtoReq;
import com.example.task.board.model.BoardEntity;
import com.example.task.board.service.BoardService;
import com.example.task.secuirty.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;


@RequiredArgsConstructor
@RestController
@Controller
public class BoardController {
    private final BoardService boardService;
    private final JwtTokenProvider jwtTokenProvider;

    //모든 게시글 조회
    @GetMapping("/api/boards")
    public List<BoardDtoRes> boardList(){
        return boardService.boardList();
    }

    //게시글 작성
    @PostMapping("/api/boards")
    public BoardDtoRes boardPost(@RequestHeader(value="Authorization")String header,
                                 @RequestParam("content") String content,
                                 @RequestParam("layout") Integer layout,
                                 @RequestParam("image") MultipartFile imgFile) throws Exception{

        String email = jwtTokenProvider.getUserPk(header);

        BoardDtoReq boardDtoReq = new BoardDtoReq();
        boardDtoReq.setEmail(email);
        boardDtoReq.setContent(content);
        boardDtoReq.setLayout(layout);

        if(!imgFile.isEmpty()) {
            try {
                String projectPath = System.getProperty("user.dir") + "/src/main/resources/static/files";

                UUID uuid = UUID.randomUUID();
                String fileName = uuid + "_" + imgFile.getOriginalFilename();

                File saveFilePath = new File(projectPath, fileName);
                imgFile.transferTo(saveFilePath);

                boardDtoReq.setImgName(fileName);
                boardDtoReq.setImgPath("/files/" + fileName);
                return boardService.boardSave(boardDtoReq);
            }catch(IOException e){
                throw new RuntimeException("Could not store file : " + imgFile.getOriginalFilename());
            }

        }else if(imgFile.isEmpty()){
            return boardService.boardSave(boardDtoReq);
        }
        return null;
    }

    //게시글 수정
    @PutMapping("/api/board/{boardId}")
    public BoardDtoRes boardUpdate(@RequestHeader(value="Authorization")String header,
                                   @RequestParam("content") String content,
                                   @RequestParam("layout") Integer layout,
                                   @RequestParam("image") MultipartFile imgFile,
                                   @PathVariable Long boardId) throws Exception {

        String email = jwtTokenProvider.getUserPk(header);

        BoardDtoReq boardDtoReq = new BoardDtoReq();
        boardDtoReq.setEmail(email);
        boardDtoReq.setContent(content);
        boardDtoReq.setLayout(layout);

        if(!imgFile.isEmpty()) {
            try {
                String projectPath = System.getProperty("user.dir") + "/src/main/resources/static/files";

                UUID uuid = UUID.randomUUID();
                String fileName = uuid + "_" + imgFile.getOriginalFilename();

                File saveFilePath = new File(projectPath, fileName);
                imgFile.transferTo(saveFilePath);

                boardDtoReq.setImgName(fileName);
                boardDtoReq.setImgPath("/files/" + fileName);

                return boardService.boardUpdate(boardDtoReq, Long.valueOf(boardId));
            }catch(IOException e){
                throw new RuntimeException("Could not store file : " + imgFile.getOriginalFilename());
            }

        }else if(imgFile.isEmpty()){
            return boardService.boardUpdate(boardDtoReq, Long.valueOf(boardId));
        }

        return null;
    }

    //board 상세페이지 조회
    @PutMapping("/api/boards/{boardId}")
    public BoardDtoRes boardDetail(@RequestHeader(value="Authorization")String header, @PathVariable Long boardId){
        //header에서 이메일추출
        String email = jwtTokenProvider.getUserPk(header);

        return boardService.boardFindByIdAndUserEntityEmail(boardId, email);
    }
//
    //board 해당유저의 이메일로 모든 게시물출력
    @PostMapping("/api/boardList")
    public List<BoardDtoRes> boardList(@RequestHeader(value="Authorization")String header,
                                       @RequestParam(value = "email") String targetEmail){
        String email = jwtTokenProvider.getUserPk(header);

        return boardService.boardFindAllByUserEntityEmail(email, targetEmail);
    }

    //board 게시물 삭제
    @DeleteMapping("/api/boards/{boardId}")
    public Long boardDelete(@PathVariable Long boardId, @RequestHeader(value="Authorization") String header){
        String email = jwtTokenProvider.getUserPk(header);

        return boardService.boardDeleteById(boardId, email);
    }

    //board 유저(자기자신)의 모든 게시물 삭제
    @DeleteMapping("/api/boardList")
    public void boardAllDelete(@RequestHeader(value="Authorization")String header){
        String email = jwtTokenProvider.getUserPk(header);

        boardService.boardDeleteAllByIdList(email);
    }
}
