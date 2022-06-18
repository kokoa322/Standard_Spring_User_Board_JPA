package com.example.task.board.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class LikesDto {

    private Long id;
    private Long userId;
    private Long boardId;
    private Long hit;
    private String email;

    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public LikesDto(String email, Long boardId) {
        this.email = email;
        this.boardId = boardId;
    }
}

