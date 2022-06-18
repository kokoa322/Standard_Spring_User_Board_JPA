package com.example.task.board.controller;

import com.example.task.board.dto.LikesDto;

import com.example.task.board.service.LikesService;
import com.example.task.secuirty.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class LikesController {

    private final JwtTokenProvider jwtTokenProvider;
    private final LikesService likesService;

    @GetMapping("/api/board/{boardId}/like")
    public String likesHit(@RequestHeader(value = "Authorization") String header, @PathVariable Long boardId){
        String email = jwtTokenProvider.getUserPk(header);

        return likesService.likesHit(email, boardId);
    }

}
